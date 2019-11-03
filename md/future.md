### future模式

> 执行任务立即返回执行其他业务，并且可以在未来获取到执行结果

- `future`对象,可以判断是否执行完毕，也可以获取执行结果
```java
public interface Future<T> {
    //获取结果，会阻塞到任务结束
    T get() throws InterruptedException;
    //完成任务使用传入的结果
    void done(T result) throws InterruptedException;
    //是否完成
    boolean isDone();
}
```
- `AsyncFuture` 一个异步`Future`实现类
```java
public class AsyncFuture<T> implements Future<T> {
    //完成状态标记
    private volatile boolean done = false;
    //完成结果
    private T result;
    
    public void done(T result) {
        synchronized (this) {
            this.result = result;
            this.done = true;
            this.notifyAll();
        }
    }
    @Override
    public boolean isDone()  {
        return done;
    }
    @Override
    public T get() throws InterruptedException {
        synchronized (this) {
            while (!done) {
                this.wait();
            }
        }
        return result;
    }
}
```
- `futureTask` 具体异步执行业务的对象
```java
public interface FutureTask<T>{
    T call();
}
```
- `futureService` 可以提交任务并且返回`future`
```java
public class FutureService<T> {

    public <T> Future<T> submit(final FutureTask<T> task) {
        AsyncFuture<T> asyncFuture = new AsyncFuture<>();
        new Thread(() -> {
            T result = task.call(); //异步执行任务
            asyncFuture.done(result); //执行完毕回调
        }).start();
        return asyncFuture;
    }
}
```
[演示Demo](../src/com/concurrent/design/future)