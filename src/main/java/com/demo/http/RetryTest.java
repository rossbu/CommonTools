package com.demo.http;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;

public class RetryTest {
    private Integer requestCounter = 0;
    private CloseableHttpClient httpClient;

    public static void main(String[] args) throws IOException {
        RetryTest retryTest = new RetryTest();
        retryTest.createFailingHttpClient();
        retryTest.createHttpClientWithRetryHandler();


        retryTest.givenDefaultConfiguration_whenReceivedIOException_thenRetriesPerformed();
    }

    public void givenDefaultConfiguration_whenReceivedIOException_thenRetriesPerformed() throws IOException {
        createFailingHttpClient();
        httpClient.execute(new HttpGet("https://httpstat.us/200"));
    }
    private void createFailingHttpClient() {
        this.httpClient = HttpClientBuilder
                .create()
                .addInterceptorFirst((HttpRequestInterceptor) (httpRequest, httpContext) -> requestCounter++)
                .addInterceptorLast((HttpResponseInterceptor) (httpResponse, httpContext) -> {
                    throw new IOException();
                })
                .build();
    }

    private void createHttpClientWithRetryHandler() {
        this.httpClient = HttpClientBuilder
                .create()
                .addInterceptorFirst((HttpRequestInterceptor) (httpRequest, httpContext) -> requestCounter++)
                .addInterceptorLast((HttpResponseInterceptor) (httpRequest, httpContext) -> { throw new IOException(); })
                .setRetryHandler(new DefaultHttpRequestRetryHandler(6, true))
                .build();
    }

    private void createHttpClientWithCustomRetryHandler() {
        this.httpClient = HttpClientBuilder
                .create()
                .addInterceptorFirst((HttpRequestInterceptor) (httpRequest, httpContext) -> requestCounter++)
                .addInterceptorLast((HttpResponseInterceptor) (httpRequest, httpContext) -> { throw new IOException(); })
                .setRetryHandler((exception, executionCount, context) -> {
                    if (executionCount < 5 && Objects.equals("GET", ((HttpClientContext) context).getRequest().getRequestLine().getMethod())) {
                        return true;
                    } else {
                        return false;
                    }
                })
                .build();
    }
}