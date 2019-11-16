### 并发包工具集的使用

- [CountDownLatch](#CountDownLatch)
- [CyclicBarrier](#CyclicBarrier)
- [Exchanger](#Exchanger)
- [Semaphore](#Semaphore)
- [Lock](#Lock)
- [Condition](#Condition)
- [StampedLock](#StampedLock)
- [ForkJoin](#ForkJoin)

#### `CountDownLatch`

> 是一个同步辅助工具，可以在一个或多个线程中的等待其他线程中的一组操作的完成

1. 可以在一个或者多个线程中等待多个任务的完成

```java
public class CountDownLatchTest {

    private static ExecutorService service = Executors.newFixedThreadPool(2);

    private static final CountDownLatch latch = new CountDownLatch(10);

    public static void main(String[] args) throws InterruptedException {
        //1. query data from db
        int[] data = query();
        //2. execute task queue
        for (int i = 0; i < data.length; i++) {
          	// each task can countDown()
            service.execute(new CalculateTask(data, i, latch));
        }
        //3. wait all of task down
        latch.await();
        //4. shutdown with threadPool
        service.shutdown();
        //another use ThreadPool await..
//        service.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("all of service finished!");
    }
}
```

2. 可以让两个线程中的一个线程等待另外一个线程执行完逻辑再执行自己的特定逻辑，类似异步

```java
/**
 * 可以在一个或者多个线程中等待另外一个线程完成自己的任务
 * 几种结束await()方法的方式
 * 1. countDown()到为0
 * 2. 外部线程进行打断
 * 3. 超时时间配置
 */
public class CountDownLatchTest2 {
    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final Thread t1 = new Thread(() -> {
            try {
                latch.await();
                System.out.println("await finished!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        final Thread tmp = new Thread(() -> {
            try {
                latch.await();
                System.out.println("start work");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        final Thread t2 = new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    t1.interrupt();
                    tmp.interrupt();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        });
        t1.start();
        t2.start();
        tmp.start();
        t1.join();
        t2.join();
        tmp.join();
        System.out.println("FINISHED");

    }
}
```

3. 可以监听一批任务的执行然后做一些回调，比如批量添加，可以先添加到缓存区然后到达一定次数再添加到db,但是这里`CountDownLatch`无法重置只能这样批量一次？？

```java
/**
 * 使用CountDownLatch管理多个任务的批量完成回调
 */
public class CountDownLatchTest3 {
    public static void main(String[] args) throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(5);
        Watcher watcher = new Watcher(latch);
        executorService.execute(new TaskRunnable1(latch, watcher));
        executorService.execute(new TaskRunnable2(latch, watcher));
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("Main Thread Exit!!");
    }


    static class TaskRunnable1 implements Runnable {

        private final CountDownLatch latch;
        private final Watcher watcher;

        TaskRunnable1(CountDownLatch latch, Watcher watcher) {
            this.latch = latch;
            this.watcher = watcher;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName() + " -> taskRunnable 1 ->finished!");
                    watcher.watch();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class TaskRunnable2 implements Runnable {

        private final CountDownLatch latch;
        private final Watcher watcher;

        TaskRunnable2(CountDownLatch latch, Watcher watcher) {
            this.latch = latch;
            this.watcher = watcher;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName() + " -> taskRunnable 2 ->finished!");
                    watcher.watch();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Watcher {
        private CountDownLatch latch;

        public Watcher(CountDownLatch latch) {
            this.latch = latch;
        }

        public void watch() {
            latch.countDown();
            if (latch.getCount() == 0) {
                System.out.println("update db..");
            }
        }
    }
}
```

#### `CyclicBarrier`

> 在多个线程中共同执行,并等待其他线程全都执行完毕，并且可以进行重置

```java
/**
 * 循环屏障
 * <p>
 * 在一个或者多个线程中等待到一个共同节点同时释放。相对于{@link java.util.concurrent.CountDownLatch}
 * 1. 能自动回调注入的任务，且能重置次数reset()
 * 2. 工作线程之间相互协调，而CountDownLatch则不关心。
 */
public class CyclicBarrierTest1 {
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        //传递进行的Runnable为回调方法
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(2, () -> System.out.println(Thread.currentThread().getName() + "-> finished!"));
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " -> begin work");
            try {
                Thread.sleep(10000);
                System.out.println(Thread.currentThread().getName() + "-> finished!");
                cyclicBarrier.await();
                System.out.println(Thread.currentThread().getName() + " -> CyclicBarrier finished!");
                //                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }).start();

        TimeUnit.MILLISECONDS.sleep(100);
      	//一些监控的ApI
        System.out.println(cyclicBarrier.getNumberWaiting());//0
        System.out.println(cyclicBarrier.getParties()); //2
        System.out.println(cyclicBarrier.isBroken()); //false
        //reset
        cyclicBarrier.reset();

        System.out.println(cyclicBarrier.getNumberWaiting());//0
        System.out.println(cyclicBarrier.getParties()); //2
        System.out.println(cyclicBarrier.isBroken()); //false
    }
}
```

#### `Exchanger`

> 是一个用来在一对线程中互相传递数据的工具

example 

```java
/**
 * Exchanger 用来在两线程之间传递数据 ，
 * 1. 使用exchanger(V v) , 在交换的时候会阻塞住。可以设置超时时间
 * 2. 传递的为真实引用地址，会有多线程并发问题
 * 3. 可以多次传递exchange()
 */
public class ExchangerTest {

    public static void main(String[] args) {
        final Exchanger<String> exchanger = new Exchanger<>();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " begin work.");
            try {
                //这边直接使用Exchanger.exchange()方法交换数据，且交换之后的对象不是拷贝值是源对象， 所有在两线程直接传递数据会有多线程安全问题
                System.out.println(Thread.currentThread().getName() + ":" + exchanger.exchange("message from Thread -A"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " finished work.");
        }, "A").start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " begin work.");
            try {
                System.out.println(Thread.currentThread().getName() + ":" + exchanger.exchange("message from Thread -B"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " finished work.");
        }, "B").start();

        System.out.println("Main Thread finished!");
    }
}
```

#### `Semaphore`

> 信号量，其中包含用数字表示的凭证数量，获取到了凭证才能继续线程调度，否则会阻塞

```java
/**
 * {@link Semaphore} 信号量，包含多个permit，获取到凭证才能获取到执行权，否则调用acquire方法则会block
 */
public class SemaphoreTest1 {

    public static void main(String[] args) {
        final SemaphoreLock lock = new SemaphoreLock();
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + "-> running");
                    lock.lock();
                    System.out.println(Thread.currentThread().getName() + "-> get semaphore lock");
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                    System.out.println(Thread.currentThread().getName() + "-> get released lock");
                }
            }).start();
        }
    }
  	//信号量实现的简单显示锁
    static class SemaphoreLock {
        private final Semaphore semaphore = new Semaphore(1);

        public void lock() throws InterruptedException {
            semaphore.acquire();
        }

        public void unlock() {
            semaphore.release();
        }
    }
}
```

常见`Semaphore`Api：

```text
/**
 * 信号量Api测试
 * 1. acquire(int times) -> 一次获取多个凭证   -> 可以通过在其他线程进行Thread#interrupt()打断获取信号量的阻塞
 * 2. availablePermits() -> 获取到现存的凭证数量
 * 3. getQueueLength() -> 获取到等待凭证的线程数量
 * 4. acquireUninterruptibly() -> 不接受打断的获取信号量。
 * 5. drainAcquired() -> 一次获取所有的凭证
 * 6. getWaitingThreads() -> 获取所有正在等待获取凭证的线程
 * 7. tryAcquired() -> 尝试获取凭证
 */
```

#### `Lock`

> 显示锁

##### 可重入锁`ReentrantLock`

example

```java
/**
 * 可重入锁测试
 * 1. lock 不可被打断
 * 2. unlock
 * 3. tryLock 可被打断
 * 4. lockInterruptibly 可被打断
 *
 * Monitor 功能方法
 * 1. {@link ReentrantLock#getHoldCount()} 获取当前线程持有的锁数量
 * 2. {@link ReentrantLock#getOwner()} 获取当前锁持有线程
 * 3. {@link ReentrantLock#getWaitingThreads(Condition)} 获取等待的线程集合
 * 4. {@link ReentrantLock#getQueueLength()} 获取等待的线程数
 */
public class ReentrantLockTest {
    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        final Thread t1 = new Thread(ReentrantLockTest::testLock);
        t1.start();
        TimeUnit.MILLISECONDS.sleep(50);
        final Thread t2 = new Thread(ReentrantLockTest::testLock);
        t2.start();
        TimeUnit.MILLISECONDS.sleep(50);
        t2.interrupt();

        System.out.println(lock.getQueueLength());
        //当前是否有线程在等待
        System.out.println(lock.hasQueuedThreads());
        //是否有线程获取到锁
        System.out.println(lock.isLocked());
        //是否是当前线程持有的锁
        System.out.println(lock.isHeldByCurrentThread());
    }

    private static void testLock() {
        try {
            //可被中断
            if (lock.tryLock(10, TimeUnit.SECONDS)) {
                try {
                    //获取当前线程持有该锁的次数
                    Optional.of(Thread.currentThread().getName() + " -> " + lock.getHoldCount()).ifPresent(System.out::println);
                    while (true) {

                    }
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

##### 读写锁`ReadWriterLock`

```java
/**
 * 读写锁测试
 * R W -> N
 * R R -> Y
 * W R -> N
 * W W -> N
 */
public class RDLockTest {
		//可重入读写锁
    private final ReadWriteLock rdLock = new ReentrantReadWriteLock();
    private final List<Long> data = new ArrayList<>();
    private final Lock readLock = rdLock.readLock();
    private final Lock writeLock = rdLock.writeLock();

    public static void main(String[] args) {
        RDLockTest test = new RDLockTest();
        IntStream.rangeClosed(1, 2)
                .forEach(i -> new Thread(test::write).start());
    }
    private void read() {
        try {
            readLock.lock();
            System.out.println("begin read");
            data.forEach(System.out::println);
        } finally {
            readLock.unlock();
        }
    }
    private void write() {
        try {
            writeLock.lock();
            System.out.println("begin write");
            data.add(System.currentTimeMillis());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            writeLock.unlock();
        }

    }
}
```

#### `Condition`

> 显示锁中提供的类似等待唤醒机制的实现，相当于Object类wait()/notify(),通常伴随着Lock对象的出现。

生产者消费者1 v 1 Example: 

```java
/**
 * Condition测试类 -> 模拟生产者消费者模式
 */
public class ConditionTest1 {

    private final ReentrantLock lock = new ReentrantLock(true);
    private final Condition condition = lock.newCondition();
    private static int data = 0; //share data
    private static volatile boolean noUse = true;

    /**
     * 1. 不使用condition行吗？ 不行，在非公平锁状态下无法做到生产后提醒消费者消费
     * 2. 不用lock行吗？ 不行，会直接报异常。类似Object的wait()/notify()等方法没有加synchronized
     * 3. condition.await() block住了，其他线程如何获取到锁？ 和wait()等一样会放弃cpu执行权
     *
     * @param args
     */
    public static void main(String[] args) {
        ConditionTest1 test = new ConditionTest1();
        new Thread(() -> {
            for (; ; ) {
                test.createData();
            }
        }).start();
        new Thread(() -> {
            for (; ; ) {
                test.useData();
            }
        }).start();
    }

		//模拟生产者
    private void createData() {
        try {
            lock.lock();  //类似synchronzied block
            while (noUse) {               
                condition.await();     //类似monitor.wait()
            }
            //product data
            System.out.println("P ->" + ++data);
            TimeUnit.SECONDS.sleep(1);
            noUse = true;
            condition.signal();      //类似monitor.notify()
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
	//模拟消费者
    private void useData() {
        try {
            lock.lock();
            while (!noUse) {
                condition.await();
            }
            //product data
            System.out.println("C ->" + data);
            noUse = false;
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}

```

生产者消费者n v n Example: 



```java
/**
 * Condition实现生产者消费者模式
 */
public class ConditionTest2 {
    //lock
    private static final ReentrantLock LOCK = new ReentrantLock();

    //produce and consume condition -> 构造两lock
    private static final Condition PRODUCE_COND = LOCK.newCondition();
    private static final Condition CONSUME_COND = LOCK.newCondition();
    private static final int MAX_CAPACITY = 100;
    private static final LinkedList<Long> TIMESTAMP_POOL = new LinkedList<>();


    public static void main(String[] args) {
        IntStream.rangeClosed(0, 15).forEach(i -> new Thread(() -> {
            for (; ; ) {
                product();
                sleep(1000);
            }
        }, "P-" + i).start());
        IntStream.rangeClosed(0, 5).forEach(i -> new Thread(() -> {
            for (; ; ) {
                consume();
            }
        }, "C" + i).start());

    }
    public static void sleep(long time) {
        try {
            TimeUnit.MILLISECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void product() {
        try {
            LOCK.lock();
            while (TIMESTAMP_POOL.size() >= MAX_CAPACITY) {
                PRODUCE_COND.await();
            }
            System.out.println("wait queue length -> " + LOCK.getWaitQueueLength(PRODUCE_COND));
            final long value = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + "-> product -> " + value);
            TIMESTAMP_POOL.addLast(value);
            CONSUME_COND.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LOCK.unlock();
        }
    }

    public static void consume() {
        try {
            LOCK.lock();
            while (TIMESTAMP_POOL.isEmpty()) {
                CONSUME_COND.await();
            }
            System.out.println(Thread.currentThread().getName() + "-> consume -> " + TIMESTAMP_POOL.removeFirst());
            PRODUCE_COND.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LOCK.unlock();
        }
    }
}

```



#### `StampedLock`

> Java8提供的一种显示锁，融合了`ReentrantLock`和`ReentrantReadWriterLock`，并且对于读写锁的分布不均提供了乐观读机制，在每次获取读锁之前先获取乐观锁，若没有写线程进行改变则进行读



基本读写锁实现：

```java
/**
 * java8Stamp锁的使用
 * <p>
 * 当上面使用过的{@link com.concurrent.design.readwriter.ReadWriteLock} 其中的读写线程分配差距很大的时候有可能有一方会很难抢到锁。这时候需要另一方进行乐观锁判断
 */
public class StampLockTest {

    public static final StampedLock stampedLock = new StampedLock();
    private static final List<Long> data = new ArrayList<>();

    public static void main(String[] args) {
        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        Runnable read = () -> {
            for (; ; ) {
                read();
            }
        };
        Runnable write = () -> {
            for (; ; ) {
                write();
            }
        };

        executorService.submit(read);
        executorService.submit(read);
        executorService.submit(write);
    }
    public static void read() {
        long stamp = -1;
        try {
            stamp = stampedLock.readLock();
            sleep(1000);
            System.out.println(Thread.currentThread().getName() + " -> read : " + data.stream().map(String::valueOf).collect(Collectors.joining(",", "R_", "")));
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }

    public static void write() {
        long stamped = -1;
        try {
            stamped = stampedLock.writeLock();
            data.add(System.currentTimeMillis());
            sleep(1000);
        } finally {
            stampedLock.unlockWrite(stamped);
        }
    }

    public static void sleep(long time) {
        try {
            TimeUnit.MILLISECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

```



乐观读锁实现：

```java
/**
 * java8Stamp锁的使用
 * <p>
 * 当上面使用过的{@link com.concurrent.design.readwriter.ReadWriteLock} 其中的读写线程分配差距很大的时候有可能有一方会很难抢到锁。这时候需要另一方进行乐观锁判断
 */
public class StampLockTest2 {

    public static final StampedLock stampedLock = new StampedLock();
    private static final List<Long> data = new ArrayList<>();

    public static void main(String[] args) {
        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        Runnable read = () -> {
            for (; ; ) {
                read();
            }
        };
        Runnable write = () -> {
            for (; ; ) {
                write();
            }
        };
      	//给线程池提供读远大于写的任务
        executorService.submit(read);
        executorService.submit(read);
        executorService.submit(read);
        executorService.submit(read);
        executorService.submit(read);
        executorService.submit(read);
        executorService.submit(read);
        executorService.submit(read);
        executorService.submit(read);
        executorService.submit(write);
    }
    public static void read() {
        //当读写差距很大的时候需要尝试获取乐观读锁
        long stamp = stampedLock.tryOptimisticRead();
        //校验是否有线程进行写操作
        if (stampedLock.validate(stamp)) {
            try {
							//尝试获取读锁
                stamp = stampedLock.readLock();
                System.out.println(Thread.currentThread().getName() + " -> read : " + data.stream().map(String::valueOf).collect(Collectors.joining(",", "R_", "")));
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
    }
	
    public static void write() {
        long stamped = -1;
        try {
            stamped = stampedLock.writeLock();
            data.add(System.currentTimeMillis());
            sleep(100);
        } finally {
            stampedLock.unlockWrite(stamped);
        }
    }

    public static void sleep(long time) {
        try {
            TimeUnit.MILLISECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```



#### `ForkJoin`

> 分而治之Java实现版本，核心组件为`ForkJoinPool` , `RecursiveTask` ,`RecursiveAction` ,`ForkJoinTask`

实现带返回值的分治累加

```java
/**
 * ForkJoinPool# RecursiveTask使用带返回值的分而治之求和
 */
public class ForkJoinRecursiveTest {

    private static final int MAX_THRESHOLD = 5;

    public static void main(String[] args) {
        final ForkJoinPool forkJoinPool = new ForkJoinPool();
        final ForkJoinTask<Integer> future = forkJoinPool.submit(new CalculateRecursiveTask(0, 1000));
        try {
            final Integer result = future.get();
            System.out.println(result);
            forkJoinPool.shutdown();
            forkJoinPool.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            //Runnable执行方法报错
            e.printStackTrace();
        }
    }

    /**
     * 计算任务
     */
    private static class CalculateRecursiveTask extends RecursiveTask<Integer> {

        private final int begin;
        private final int end;


        private CalculateRecursiveTask(int begin, int end) {
            this.begin = begin;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if (end - begin <= MAX_THRESHOLD) {
                return IntStream.rangeClosed(begin, end).sum();
            } else {
                //fork and join
                int mid = (end + begin) / 2;
                CalculateRecursiveTask leftTask = new CalculateRecursiveTask(begin, mid);
                CalculateRecursiveTask rightTask = new CalculateRecursiveTask(mid + 1, end);
                leftTask.fork();
                rightTask.fork();
                return leftTask.join() + rightTask.join();
            }
        }
    }
}
```

不带返回值的,使用全局变量接受累加结果！需要保证线程安全！

```java
/**
 * ForkJoin 没有返回值的任务测试类
 */
public class ForkJoinRecursiveActionTest {

    //使用全局变量来获取返回结果
    private static final AtomicLong sum = new AtomicLong(0);

    public static void main(String[] args) throws InterruptedException {
        final ForkJoinPool pool = new ForkJoinPool();
        pool.execute(new CalculateRecursiveAction(1,10000));
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);
        Optional.of(sum).ifPresent(System.out::println);
    }


    private static class CalculateRecursiveAction extends RecursiveAction {

        private final long begin;
        private final long end;
        public static final long MAX_THRESHOLD = 20;

        private CalculateRecursiveAction(long begin, long end) {
            this.begin = begin;
            this.end = end;
        }

        @Override
        protected void compute() {
            if (end - begin <= MAX_THRESHOLD) {
                LongStream.rangeClosed(begin, end).forEach(sum::getAndAdd);
            } else {
                long mid = (end + begin) / 2;
                CalculateRecursiveAction left = new CalculateRecursiveAction(begin, mid);
                CalculateRecursiveAction right = new CalculateRecursiveAction(mid + 1, end);
                left.fork();
                right.fork();
            }
        }
    }
}
```


















