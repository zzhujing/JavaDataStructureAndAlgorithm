## 读写锁设计模式

> 核心在于读写锁的编写。为了解决单线程的效率又需要线程安全，进行读写分离，具体状态如下表

两线程读写状态设计表

| 线程状态 |  READ| WRITE|
| --- | --- | ---|
| READ | unlock | lock|
| WRITE| lock|  lock|

#### 根据上面的状态表可以设计出一个简易的`ReadWriteLock`
```java
/**
 * 自定义读写锁
 */
public class ReadWriteLock {
    
    //正在读取数
    private int readingReader;
    //正在等待数
    private int waitingReader;
    //正在写入数
    private int writingWriter;
    //等待写入数
    private int waitingWriter;
    //是否写入优先，因为正常业务下读线程会远大于写线程，此时容易读线程一直获取cpu执行权
    private boolean preferWrite;

    public ReadWriteLock() {
        this(true);
    }

    public ReadWriteLock(boolean preferWrite) {
        this.preferWrite = preferWrite;
    }
    //读锁
    public synchronized void readLock() throws InterruptedException {
        this.waitingReader++;
        try {
            //写入优先优化
            while (writingWriter > 0 || (preferWrite && waitingWriter > 0)) {
                this.wait();
            }
            this.readingReader++;
        } finally {
            this.waitingReader--;
        }
    }
    
    //释放读锁，唤醒其他wait()线程
    public synchronized void unReadLock() {
        this.readingReader--;
        this.notifyAll();
    }
    
    //写同理
    public synchronized void writeLock() throws InterruptedException {
        this.waitingWriter++;
        try {
            while (readingReader > 0 || writingWriter > 0) {
                this.wait();
            }
            this.writingWriter++;
        } finally {
            this.waitingWriter--;
        }
    }

    public synchronized void unWriteLock() {
        this.writingWriter--;
        this.notifyAll();
    }


}
```
[演示Demo](../src/main/java/com/concurrent/design/readwriter/ReadWriteClient.java)


