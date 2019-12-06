import com.concurrent.MyLock;
import com.concurrent.thread.lock.BooleanLock;
import com.concurrent.thread.lock.Lock;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class TestLock {
    public static void main(String[] args) {
        final List<Integer> result = Arrays.asList(1, 2, 3);
        List<Integer> tmp = result;
        System.out.println(tmp.size());
    }
}
