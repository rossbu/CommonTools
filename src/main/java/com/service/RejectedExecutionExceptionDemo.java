package com.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.stream.Collectors.toList;

public class RejectedExecutionExceptionDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final List<String> urls = List.of("https://stackoverflow.com", "https://stackoverflow.com", "https://stackoverflow.com");

        // This succeeds on JDK 16, 17
        retrieveMany(urls, 4);

        System.out.println("----");

        // This fails on JDK 17, but succeeds on 16
        retrieveMany(urls, 3);
    }

    private static List<String> retrieveMany(List<String> urls, int threads) throws InterruptedException, ExecutionException {
        return new ForkJoinPool(threads, ForkJoinPool.defaultForkJoinWorkerThreadFactory, (t, e) -> {}, true, 0, threads, 1, null, 1, MINUTES)
                .submit(() -> urls.parallelStream()
                        .map(url -> {
                            try {
                                return HttpClient.newBuilder().build().send(HttpRequest.newBuilder(URI.create(url)).build(), BodyHandlers.ofString()).body();
                            } catch (IOException | InterruptedException aE) { }
                            return null;
                        })
                        .collect(toList()))
                .get();
    }

}