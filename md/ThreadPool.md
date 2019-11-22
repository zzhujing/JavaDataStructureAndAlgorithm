### 线程池相关

- [Executor](#Executor)
- [ExecutorService](#ExecutorService)
- [ThreadPoolExecutor](#ThreadPoolExecutor)
- [Executors](#Executors)



基本的类图如下所示

![image-20191122162248149](/Users/hujing/Library/Application Support/typora-user-images/image-20191122162248149.png)

#### `Executor`

> 只提供一个`execute(Runnable task)` 方法来接受任务，具体任务怎么直接看子类实现

#### `ExecutorService`

> 在`Executor`上添加了一些关闭和执行任务的遍历方法，并且引入的`Fture`模式。

#### `AbstractExecutorService`

> ExecutorService的简单实现，其中对Runnable/Callable进行了包装成`FutureTask`

#### `ThreadPoolExecutor`

> 线程池的核心实现，其中核心包括`Work Thread`，`keepAliveTime`, `TaskQueue(BlockingQueue)`,`RejectPolicy`,`ThreadFactory`等核心属性

```java
        /**
         * 下面七个主要的线程池参数
         * int corePoolSize,  核心线程数量
         * int maximumPoolSize, 最大worker线程数量
         * long keepAliveTime,  超出核心线程的线程空闲时间
         * TimeUnit unit,   时间单位
         * BlockingQueue<Runnable> workQueue, 任务队列，里面使用显示锁+Condition实现了阻塞队列
         * ThreadFactory threadFactory, 线程工厂
         * RejectedExecutionHandler handler 拒绝策略
         */
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                1, 2, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1),
                new MyThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy() //拒绝策略
        );
```

注意点：

- `CorePoolSize` : 在通常情况下是不会被回收的，即使是空闲状态，除非设置了`threadPoolExecutor.allowCoreThreadTimeOut(true)`
- `maximumPoolSize` : 只有在`CorePoolSize`全部都在任务且`TaskBlockingQueue`任务满了才会创建新的线程
- `keepAliveTime` ： 空闲回收时间，会收回在`CorePoolSize -> MaximumPoolSize` 之间的空闲线程
- `BlockingQueue` : 阻塞的任务队列，没有任务会阻塞，任务满了也会阻塞，里面的实现用的是显示锁+`Condition`
- `ThreadFactory` : 可以自定义`WorkerThread`的创建工厂，可以给里面的工作线程添加一些自己的策略，比如守护线程，设置捕捉任务线程异常的handler等。
- `RejectPolicy` ： 拒绝策略。自带的有四种：
  - `AbortPolicy` ： 直接拒绝
  - `DiscardPolicy` ： 直接吞掉，不给报错信息
  - `DiscardOldestPolicy` : `poll`掉任务队列中的最后一个，然后执行新任务
  - `CallerRunsPolicy` : 使用主线程来执行

| 常用方法                   | 用处                                                         |
| -------------------------- | ------------------------------------------------------------ |
| getActiveCount             | 获取到正在处理任务的线程                                     |
| shutdown                   | 非阻塞，打断空闲线程，然后执行完所有正在执行的Task           |
| shutdownNow                | 非阻塞，会打断所有线程，并且将所有的任务队列中的Task返回，那些正在执行的任务无法获取到 |
| allowCoreThreadTimeOut     | 设置为true允许回收空闲的核心线程                             |
| remove                     | 移除任务队列中的任务                                         |
| prestartCoreThread         | 激活一个核心。即使此时没有任务                               |
| beforeExecute/afterExecute | 在线程池执行方法的前后进行增强                               |
| invokeAny /invokeAll       | 阻塞的执行一批任务。可以通过返回的Future来取消或者中断       |
| getQueue                   | 可以获取到里面的任务队列来手动添加任务，前提是要有活跃的线程。 |

####  `Executors`




