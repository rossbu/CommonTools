package com.demo.multithreading.httpserver;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class DummyHttpServer {

    private static final int PORT = 8000;
    private static final int MAX_CONCURRENT_REQUESTS = 5;
    private static final Semaphore semaphore = new Semaphore(MAX_CONCURRENT_REQUESTS);
    private static int requestCounter = 0;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/test", new MyHandler());
        ExecutorService executor = Executors.newFixedThreadPool(MAX_CONCURRENT_REQUESTS);
        server.setExecutor(executor);
        server.start();
        System.out.println("Server started on port " + PORT);
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            int currentRequestNumber;
            synchronized (DummyHttpServer.class) {
                currentRequestNumber = ++requestCounter;
            }
            try {
                semaphore.acquire();
                System.out.println("Handling request " + currentRequestNumber + " from " + exchange.getRemoteAddress());
                Thread.sleep(8000); // Simulate long-running task
                String response = "Request handled";
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                String response = "Request interrupted";
                exchange.sendResponseHeaders(500, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (IOException e) {
                String response = "Client aborted request";
                exchange.sendResponseHeaders(499, response.length()); // Custom error code for client abort
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } finally {
                semaphore.release();
            }
        }
    }
}