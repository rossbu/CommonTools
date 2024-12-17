package com.demo.multithreading.httpserver;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncHttpClientExample {

    private static final int POOL_SIZE = 5;
    private static final String SERVER_URL = "http://localhost:8000/test";
    private static final Duration TIMEOUT = Duration.ofSeconds(15);

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE);
        HttpClient client = HttpClient.newBuilder()
                .executor(executor)
                .connectTimeout(TIMEOUT)
                .build();

        CompletableFuture<?>[] futures = new CompletableFuture<?>[POOL_SIZE];
        for (int i = 1; i <= POOL_SIZE; i++) {
            final int id = i;
            futures[i - 1] = sendRequest(client, "Req" + id);
        }

//        The issue is likely that the main thread is exiting before the asynchronous requests complete.
//        To ensure the main thread waits for all requests to complete
        CompletableFuture.allOf(futures).join();
        executor.shutdown();
    }

    private static CompletableFuture<Void> sendRequest(HttpClient client, String threadName) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL))
                .timeout(TIMEOUT)
                .GET()
                .build();

        System.out.println(threadName + " sending request to " + SERVER_URL);

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    System.out.println(threadName + " received response: " + response.body());
                }).exceptionally(e -> {
                    System.err.println(threadName + " failed to send request: " + e.getMessage());
                    return null;
                });
    }
}