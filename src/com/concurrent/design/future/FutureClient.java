package com.concurrent.design.future;

public class FutureClient {
    public static void main(String[] args) throws InterruptedException {
        FutureService<String> futureService = new FutureService<>();
        futureService.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "FINISH";
        }, System.out::println);

        System.out.println("do other things");
        Thread.sleep(500);
    }
}
