package com.concurrent.juc.threadpool;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

 /**
 * Question:
 * 如果在任务线程池shutdown之后获取到所有未执行以及没执行完毕的Task？
 * Answer:
 * 使用自定义Task然后使用一个标记值去表示Task执行完毕。不可使用ExecutorService#shutdownNow()返回的List<Runnable>
 */
public class FailureRunnableReturnTest {
    public static void main(String[] args) throws InterruptedException {

        /*final ExecutorService executorService = Executors.newSingleThreadExecutor();

        final List<Runnable> runnableList = IntStream.range(0, 5).boxed().map(FailureRunnableReturnTest::buildTask).collect(Collectors.toList());

        CompletionService<Object> completionService = new ExecutorCompletionService<>(executorService);

        runnableList.forEach(r -> completionService.submit(Executors.callable(r)));

//        Future<Object> f = null;
//        while ((f = completionService.take()) != null) {
//            System.out.println(f.get());
//        }

        TimeUnit.SECONDS.sleep(12);
        //获取到所有未执行完的任务
        //使用shutdownNow()不能获取到被打断失败的任务
        executorService.shutdownNow();
        */

        //使用自定义任务标志来获取到所有的任务（包括在队列中未执行和被打断执行失败的。）

        final ExecutorService executorService = Executors.newSingleThreadExecutor();

        final List<Callable<Integer>> callableList = IntStream.range(0, 5).boxed().map(MyCallable::new).collect(Collectors.toList());

        CompletionService<Integer> completionService = new ExecutorCompletionService<>(executorService);

        callableList.forEach(completionService::submit);

        TimeUnit.SECONDS.sleep(12);
        executorService.shutdownNow();
        callableList.stream().filter(c->!((MyCallable)c).isSuccess()).forEach(System.out::println);
    }

    private static class MyCallable implements Callable<Integer> {

        private final Integer value;
        private boolean success = false;

        private MyCallable(Integer value) {
            this.value = value;
        }

        @Override
        public Integer call() throws Exception {
            System.out.println("begin..");
            TimeUnit.SECONDS.sleep(value * 10 + 10);
            System.out.println("end..");
            success = true;
            return value;
        }

        public boolean isSuccess() {
            return success;
        }
    }

    private static Runnable buildTask(Integer i) {
        return () -> {
            try {
                System.out.println("begin..");
                TimeUnit.SECONDS.sleep(i * 10 + 10);
                System.out.println("end..");
            } catch (InterruptedException e) {
                System.out.println("interrupt...");
            }
        };
    }
}
