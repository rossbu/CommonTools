package com.demo.async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CompletableFutureAllOfDemo {

    public static void main(String[] args) {

    }

    public CompletableFuture<String> findSomeValue() {
        return CompletableFuture.supplyAsync(() -> {
            sleep(1);
            return "Niraj";
        });
    }

    private void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void completableFutureAllof() {
        List<CompletableFuture<String>> list = new ArrayList<>();

        IntStream.range(0, 5).forEach(num -> {
            list.add(findSomeValue());
        });

    CompletableFuture<Void> future1 =
        CompletableFuture.failedFuture(new RuntimeException("Exception"));
        CompletableFuture<Void> allfuture = CompletableFuture.allOf(list.toArray(new CompletableFuture[list.size()]));//Created All of object
        CompletableFuture<List<String>> allFutureList = allfuture.thenApply(val -> {
            System.out.println("Creating List");
            return list.stream().map(f -> f.join()).collect(Collectors.toList());
        });

        System.out.println("List creadted , now joing the vlaues");
        CompletableFuture<String> futureHavingAllValues = allFutureList.thenApply(fn -> {
            System.out.println("I am here");
            return fn.stream().collect(Collectors.joining());
        });
        String concatenateString = futureHavingAllValues.join();
        System.out.println("finally: result: "+concatenateString);
//        assertEquals("NirajNirajNirajNirajNiraj", concatenateString);
//

    }
}
