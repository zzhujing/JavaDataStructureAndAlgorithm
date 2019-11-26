package com.concurrent.blockingqueue;

import org.junit.Test;

import java.util.concurrent.*;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * {@link java.util.concurrent.LinkedTransferQueue} 测试
 * 1. 是一个能判断添加的时候是否被消费，通过{@link java.util.concurrent.LinkedTransferQueue#tryTransfer(Object)}
 */
public class LinkedTransferQueueTest {


    @Test
    public void testTryTransfer() throws InterruptedException {
        LinkedTransferQueue<String> transferQueue = new LinkedTransferQueue<>();
        final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.schedule(transferQueue::take, 1, TimeUnit.SECONDS);
        scheduledExecutorService.shutdown();
        TimeUnit.MILLISECONDS.sleep(1100);
        System.out.println(transferQueue.tryTransfer("Java"));
    }

    @Test
    public void testTransferAndTake() throws InterruptedException {
        LinkedTransferQueue<String> transferQueue = new LinkedTransferQueue<>();
        final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.schedule(() -> {
            try {
                assertThat(transferQueue.take(), equalTo("Java"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, TimeUnit.SECONDS);
        service.shutdown();
        transferQueue.transfer("Java");
    }

    @Test
    public void testMonitor() throws InterruptedException {
        LinkedTransferQueue<String> transferQueue = new LinkedTransferQueue<>();
        assertThat(transferQueue.getWaitingConsumerCount(), equalTo(0));
        assertThat(transferQueue.hasWaitingConsumer(), equalTo(false));
        final ExecutorService executorService = Executors.newFixedThreadPool(5);
        IntStream.range(0, 5).boxed().map(String::valueOf).map(s -> (Callable) () -> {
            try {
                return transferQueue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).forEach(executorService::submit);
        TimeUnit.MILLISECONDS.sleep(100);
        assertThat(transferQueue.getWaitingConsumerCount(), equalTo(5));
        assertThat(transferQueue.hasWaitingConsumer(), equalTo(true));
    }
}
