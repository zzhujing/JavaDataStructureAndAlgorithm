package com.concurrent.juc.threadpool;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * CompletionServiceTest
 * 1. java8 之前处理future list提交之后的无序返回。
 */
public class CompletionServiceTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        List<Callable<Event>> callableList = Arrays.asList(
                () -> {
                    TimeUnit.SECONDS.sleep(5);
                    System.out.println("5 finished.");
                    return new Event("5", null);
                },
                () -> {
                    TimeUnit.SECONDS.sleep(4);
                    System.out.println("4 finished.");
                    return new Event("4", null);
                },
                () -> {
                    TimeUnit.SECONDS.sleep(6);
                    System.out.println("6 finished.");
                    return new Event("6", null);
                }
        );
        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        final ExecutorCompletionService<Event> executorCompletionService = new ExecutorCompletionService<>(executorService);
        List<Future<Event>> futureList = new ArrayList<>();
        callableList.forEach(c -> futureList.add(executorCompletionService.submit(c)));

        Future<Event> take = null;
        while ((take = executorCompletionService.take()) != null) {
            System.out.println(take.get().eventId);
        }
    }

    private static class Event {
        final private String eventId;
        final private String msg;

        private Event(String eventId, String msg) {
            this.eventId = eventId;
            this.msg = msg;
        }

        public String getEventId() {
            return eventId;
        }

        public String getMsg() {
            return msg;
        }
    }
}
