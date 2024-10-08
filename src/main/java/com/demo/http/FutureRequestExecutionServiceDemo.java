package com.demo.http;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.FutureRequestExecutionMetrics;
import org.apache.http.impl.client.FutureRequestExecutionService;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpRequestFutureTask;
/**
 * https://www.cnblogs.com/papering/p/9963987.html
 * Preface http://hc.apache.org/httpcomponents-client-ga/tutorial/html/preface.html
 */
public class FutureRequestExecutionServiceDemo {
        public static void main(String[] args) {
            FutureRequestExecutionServiceDemo demo = new FutureRequestExecutionServiceDemo();
            demo.test();
        }

    private void test() {
        HttpClient httpClient = HttpClientBuilder.create().setMaxConnPerRoute(5).build();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        FutureRequestExecutionService futureRequestExecutionService =
                new FutureRequestExecutionService(httpClient, executorService);
        HttpRequestFutureTask<Void> execute = futureRequestExecutionService.execute(new HttpGet("http://www.google.com"), HttpClientContext.create(), new DummyResponseHander());
        FutureRequestExecutionMetrics metrics = futureRequestExecutionService.metrics();
        System.out.println("Active connection count: " + metrics.getActiveConnectionCount());
        System.out.println("Scheduled connection count: " + metrics.getScheduledConnectionCount());
        System.out.println("Successful connection count: " + metrics.getSuccessfulConnectionCount());
        System.out.println("Successful connection average duration: " + metrics.getSuccessfulConnectionAverageDuration());
        System.out.println("Failed connection count: " + metrics.getFailedConnectionCount());
        System.out.println("Failed connection average duration: " + metrics.getFailedConnectionAverageDuration());
        System.out.println("Task count: " + metrics.getTaskCount());
        System.out.println("Request count: " + metrics.getRequestCount());
        System.out.println("Request average duration: " + metrics.getRequestAverageDuration());
        System.out.println("Task average duration: " + metrics.getTaskAverageDuration());
        executorService.shutdown();

//        metrics.getActiveConnectionCount(); // currently active connections
//        metrics.getScheduledConnectionCount(); // currently scheduled connections
//        metrics.getSuccessfulConnectionCount(); // total number of successful requests
//        metrics.getSuccessfulConnectionAverageDuration(); // average request duration
//        metrics.getFailedConnectionCount(); // total number of failed tasks
//        metrics.getFailedConnectionAverageDuration(); // average duration of failed tasks
//        metrics.getTaskCount(); // total number of tasks scheduled
//        metrics.getRequestCount(); // total number of requests
//        metrics.getRequestAverageDuration(); // average request duration
//        metrics.getTaskAverageDuration(); // average task duration
    }

    private class DummyResponseHander implements ResponseHandler<Void> {
          @Override
          public Void handleResponse(org.apache.http.HttpResponse response) {
                System.out.println("Response received");
                response.setStatusCode(200);
                return null;
          }
    }
}
