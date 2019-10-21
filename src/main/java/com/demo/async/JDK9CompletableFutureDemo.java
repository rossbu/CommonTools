package com.demo.async;

import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class JDK9CompletableFutureDemo extends FutureBase{
    public static void main(String[] args) throws InterruptedException {

        // For example, I want to calculate the sum of two numbers and for some conditions, I want it to happen after 2 seconds, my code will look like this:
        // When running the above example, you will see after 2s, the results will appear:
        int a = 2;
        int b = 5;

        Supplier<Integer> integerSupplier = () -> a + b;
        Executor executor = CompletableFuture.delayedExecutor(2, TimeUnit.SECONDS);
        CompletableFuture.supplyAsync(integerSupplier, executor).thenAccept(result -> System.out.println(result));
        TimeUnit.SECONDS.sleep(10);
    }

    public void createMapWithMapOf() {
        Map<String, String> emptyMap = Map.of();
        Map<String, String> singletonMap = Map.of("key1", "value");
        Map<String, String> map = Map.of("key1","value1", "key2", "value2");
    }
    public void createMapWithMapEntries() {
        Map<String, String> map = Map.ofEntries(
                new AbstractMap.SimpleEntry<String, String>("name", "John"),
                new AbstractMap.SimpleEntry<String, String>("city", "budapest"),
                new AbstractMap.SimpleEntry<String, String>("zip", "000000"),
                new AbstractMap.SimpleEntry<String, String>("home", "1231231231")
        );
    }
}
