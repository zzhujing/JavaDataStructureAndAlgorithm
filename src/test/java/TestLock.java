import com.concurrent.MyLock;
import com.concurrent.thread.lock.BooleanLock;
import com.concurrent.thread.lock.Lock;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class TestLock {
    public static void main(String[] args) {
       /* final BooleanLock lock = new BooleanLock();
        IntStream.range(1, 5).boxed()
                .forEach(i -> new Thread(() -> {
                    try {
                        lock.lock(1000);
                        TimeUnit.SECONDS.sleep(3);
                        System.out.println("Hello , Thread In Action.");
                    } catch (InterruptedException | Lock.TimeOutException e) {
                        //ignore
                    } finally {
                        try {
                            lock.unlock();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start());*/
        String a = "Hello";
        a += "World";
        String b = "HelloWorld";
        System.out.println(a==b);
        System.out.println(a.equals(b));
    }
}
