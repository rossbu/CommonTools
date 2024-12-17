package com.demo.multithreading.httpserver;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class HttpClientSimulator {

    private static final int POOL_SIZE = 5;
    private static final String SERVER_URL = "http://localhost:8000/test";

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE);
        List<Future<?>> futures = new ArrayList<>();
        List<Integer> elapsedTimes = new ArrayList<>();

        for (int i = 1; i <= POOL_SIZE; i++) {
            final int id = i;
            Future<?> future = executor.submit(() -> {
                try {
                    sendRequest("Req" + id);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            futures.add(future);
            elapsedTimes.add(0);
        }

        // Simulate urgent request T6
        Thread.sleep(1000); // Wait for a moment before sending T6
        Thread urgentRequest = new Thread(() -> {
            try {
                sendRequest("urgent-6");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        urgentRequest.start();

        // Periodically print the status of each thread with elapsed time
        new Thread(() -> {
            boolean allDone;
            do {
                allDone = true;
                synchronized (elapsedTimes) {
                    for (int i = 0; i < futures.size(); i++) {
                        Future<?> future = futures.get(i);
                        int elapsedTime = elapsedTimes.get(i);
                        System.out.println("Thread Req" + (i + 1) + " is " + (future.isDone() ? "done" : "running") + " (elapsed time: " + elapsedTime + " seconds)");
                        if (!future.isDone()) {
                            elapsedTimes.set(i, elapsedTime + 1);
                            allDone = false;
                        }
                    }
                }
                try {
                    Thread.sleep(1000); // Check every 1 second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } while (!allDone);
        }).start();

        // Interrupt one of the existing threads to simulate urgency
        Thread.sleep(1000); // Wait for a moment before interrupting
        if (!futures.isEmpty()) {
            futures.get(0).cancel(true); // Interrupt the first thread
            System.out.println("Thread Req1 was interrupted by main thread");
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        executor.shutdownNow();
    }

    private static void sendRequest(String threadName) throws IOException {
        try {
            System.out.println(threadName + " is sending request");
            HttpURLConnection connection = (HttpURLConnection) new URL(SERVER_URL).openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (Thread.currentThread().isInterrupted()) {
                System.out.println(threadName + " was interrupted in try block");
                return;
            }
            System.out.println(threadName + " received response code: " + responseCode);
            connection.disconnect();
        } catch (Exception e) {
            if (Thread.currentThread().isInterrupted()) {
                System.out.println(threadName + " was interrupted in exception block");
            } else {
                throw e;
            }
        }
        // Check for interruption after the request
        if (Thread.currentThread().isInterrupted()) {
            System.out.println(threadName + " was interrupted after sending request");
        }
    }
}