### ThreadLocal模式
> 保存线程独有的上下文对象，达到线程隔离

- 内部结构是一以当前线程作为`key`的`Map`对象 , ex : `
Map<Thread.currentThread(),T>`
- 使用场景: 一般用于多个业务中都需要操作的上下文对象

[演示Demo](../src/com/concurrent/design/threadlocal/ContextClient.java)

### Guarded Suspension模式
> 使用任务队列来接受请求，若请求队列为空则等待

[演示Demo](../src/com/concurrent/design/suspension/SuspensionClient.java)

### Balking模式
> 控制一个任务只由一个消费者消费,只需要简单的在消费的时候进行判断即可

[演示Demo](../src/com/concurrent/design/balking/BalkingClient.java)

### 生产者消费模式

> 使用队列来保存生产消息，多个生产消费线程同时工作

[演示Demo](../src/com/concurrent/design/prodandcons/ProducerAndConsumerClient.java)

### CountDown模式

> 使用计数的方式来让主线程等待工作线程全部执行完毕，类似`join`的效果

[演示Demo](../src/com/concurrent/design/countdown/CountDownClient.java)

### ThreadPreMessage
> 每一个消息使用对应不同线程处理

[演示Demo](../src/com/concurrent/design/threadpremessage/ThreadPreMessageClient.java)

### Worker Thread模式

> 类似于生产者消费者

[演示Demo](../src/com/concurrent/design/work/WorkerPatternClient.java)