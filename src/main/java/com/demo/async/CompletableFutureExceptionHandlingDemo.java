package com.demo.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CompletableFutureExceptionHandlingDemo {
    ExecutorService executorService = Executors.newFixedThreadPool(3);
    public static void main(String[] args) {
        CompletableFutureExceptionHandlingDemo demo = new CompletableFutureExceptionHandlingDemo();
        demo.notifyUser();
    }


    /**
     * this is for JDK9 only
     * Java 9 has added orTimeout and completeOnTimeout methods to handel this.
     * @return
     */
    public CompletableFuture<Integer> findUnknowUser(){
        return CompletableFuture.supplyAsync(this::findUserId)
                .orTimeout(1, TimeUnit.MINUTES);
    }

    public int findUserId() {
        // take 3 seconds to find the user
        System.out.println(Thread.currentThread() + " : findUserId");
        sleep(3);
        return 2;
    }

    public String findUserEmailByUserId(int userId) {
        // take 5 seconds to get email
        System.out.println(Thread.currentThread() + ": findUserEmailByUserid");
        sleep(4);
        return "tbu@abc.com";
    }

    public String notifyUserByEmail(String userEmail) {
        System.out.println(Thread.currentThread() + ": notifyUserByEmail");
        sleep(5);
        if (true) {
            throw new RuntimeException("Invalid userEmail Exception");
        }
        return "message sent";
    }

    public void consumeReturnedMessage(String message){
        System.out.println(Thread.currentThread() + " : consumeReturnedMessage");
        sleep(2);
        System.out.println("message is read by the boss");
    }

    private void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public CompletableFuture<String> findFirstName() {

        return CompletableFuture.supplyAsync(() -> {
            sleep(1);
            System.out.println(Thread.currentThread() + " findFirstName");
            return "Me";
        });

    }

    public CompletableFuture<String> findLastName() {

        return CompletableFuture.supplyAsync(() -> {
            sleep(1);
            System.out.println(Thread.currentThread() + " findLastName");
            return "MEME";
        });
    }



    public void notifyUser() {
        CompletableFuture<String> processUser = CompletableFuture
                .supplyAsync(this::findUserId, executorService)
                .thenApplyAsync(this::findUserEmailByUserId, executorService)
                .thenApplyAsync(this::notifyUserByEmail)
                .exceptionally(ex -> {
                    System.out.println("error: " + ex.getMessage());
                    return "failed to notifyUser";
                });
        String result = processUser.join();
        System.out.println(result); // message sent

        System.out.println("-------------------- Now let's try one more time with handler --------------------");


        // you can use whenComplete to replace handleAsync
        String result1 = CompletableFuture
                .supplyAsync(this::findUserId)
                .thenApplyAsync(this::findUserEmailByUserId)
                .thenApply(this::notifyUserByEmail)
                .handleAsync((response, ex) -> {
                    System.out.println("handleAsync : ");
                    if (response != null) {
                        System.out.println("got result");
                    } else {
                        System.out.println("handleAsync error: " + ex.getMessage());
                    }
                    return response;
                }).join();
        System.out.println(result1);



        System.out.println("-------------------- Consuming returned message --------------------");
        Void result2 = CompletableFuture
                .supplyAsync(this::findUserId)
                .thenApplyAsync(this::findUserEmailByUserId)
                .thenApply(this::notifyUserByEmail)
                .thenAccept(this::consumeReturnedMessage).whenComplete((response, ex) -> {
                    System.out.println("Ok all done!");
                }).join();

        System.out.println("-------------------- Combine 2 futures together --------------------");
        String fullname = findFirstName().thenCombine(findLastName(), (firstName, lastName) -> {
            String x = firstName + lastName;
            System.out.println(x);
            return x;
        }).join();
        System.out.println("fullname is :" + fullname);


    }

}
