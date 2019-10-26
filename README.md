### Thread和runnable的关系
#### runnable是什么？为什么有了Thread还需要Runnable对象？为什么我们重写的是run()方法，却还是要调用start()方法来启动线程呢？

`Runnable`是使用了类似策略模式将一个线程中要实现的业务代码和业务参数都提取到了一个`Runnable`类中，`Thread类`中会判断传入的`Runnable`是否为空，不为空则会调用在调用`start()`方法的时候，被`native`方法的`start0()`回调`Thread#run()`中调用`Runnable target #run()`,如下面代码块所示

```java
    public void run() {
        if (target != null) {
            target.run();
        }
    }
```

#### Thread线程的状态以及其中的切换示意图

![f46b91bd0408da65f3bc24b00a85329f.png](evernotecid://B5ED5B53-EE4A-4F05-B17D-9DADE01BF1D5/appyinxiangcom/26506480/ENResource/p27)

#### Thread中用到的策略模式,Thread通过传入不同的runnable对象来完成不同的线程任务调度，其中传入的runnable对象相当于传入不同的策略模式对象

#### Thread构造方法中的细节
- ThreadGroup，不传递默认是父线程的ThreadGroup对象
- ThreadName , 线程的名字不传递的话默认为Thread-"一个从0递增的数字"
-  stackSize , 值得是该线程可拥有的最大虚拟机栈的大小 ,每个线程都有自己的虚拟机栈，但是JVM的栈内存的大小是固定的，所以通过调整线程的最大虚拟机栈来调节该线程可包含的内存大小

#### 守护线程
> 可以通过thread实例对象的deamon(boolean b)来设置，如果一个线程被申明为守护线程，那么该线程的主线程退出之后守护线程也会退出，可以用该特别来实现类似连接心跳的实现，当连接建立的时候后台线程定时调用发送心跳包，当连接死掉，心跳也随之结束

#### ThreadApI

- `join()` : 这个方法是用来阻塞父线程，等待子线程执行

```java
public class ThreadJoinDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            IntStream.range(1, 1000).forEach(System.out::println);
            System.out.println("child thread finish done.");
        });
        t1.start();
        t1.join(); //Main线程会等待t1结束之后再开始执行
        IntStream.range(1, 1000).forEach(System.out::println);
    }
}
```

- `interrupt()` : 这个方法用来中断被`sleep()`,`wait()`,`join()`,阻塞住的线程，并抛出`InterruptedException`异常
```java
public class ThreadInterruptDemo {

    private static final Object MONITOR = new Object();

    public static void main(String[] args) {
        Thread mainThread = Thread.currentThread();
        Thread t = new Thread(() -> {
            while (true) {
                }
        });

        //在join阻塞Main线程之后使用第二个线程中断join
        Thread t2 = new Thread(()->{
            try {
                //这里等待是避免还没开始join()就已经调用的interrupt()
                Thread.sleep(1000L);
                System.out.println(mainThread.isInterrupted());
                //中断Main线程的join阻塞
                mainThread.interrupt();
                System.out.println(mainThread.isInterrupted());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();
        t2.start();
        try {
            //阻塞Main线程等待t线程调用完毕
            t.join();
        } catch (InterruptedException e) {
            System.out.println("join interrupt");
            e.printStackTrace();
        }
        System.out.println("Main Thread Done..");
    }
}
```

