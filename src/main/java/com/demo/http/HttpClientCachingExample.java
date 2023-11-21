package com.demo.http;


import org.apache.http.client.cache.CacheResponseStatus;
import org.apache.http.client.cache.HttpCacheContext;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.CachingHttpClients;

import java.io.IOException;

/**
 * This example demonstrates how to use caching {@link CacheConfig}.
 * https://hc.apache.org/httpcomponents-client-4.5.x/current/tutorial/html/caching.html
 *
 */
public class HttpClientCachingExample {

    public static void main(String... args) throws IOException {

        CacheConfig cacheConfig = CacheConfig.custom()
                .setMaxCacheEntries(3000)
                .setMaxObjectSize(10240) // 10MB
                .setSharedCache(false)
                .setAllow303Caching(true)
                .setHeuristicCachingEnabled(true)
                .build();

        CloseableHttpClient cachingClient = CachingHttpClients.custom()
                .setCacheConfig(cacheConfig)
                .build();

        for (int i = 0; i < 3; i++){
            HttpCacheContext context = HttpCacheContext.create();
            HttpGet httpget = new HttpGet("http://httpbin.org/cache");
            System.out.println("Executing request " + httpget.getRequestLine());
            CloseableHttpResponse response = cachingClient.execute(httpget, context);
            try {
                System.out.println("----------------------------------------");
                CacheResponseStatus responseStatus = context.getCacheResponseStatus();
                switch (responseStatus) {
                    case CACHE_HIT:
                        System.out.println("A response was generated from the cache with " +
                                "no requests sent upstream");
                        break;
                    case CACHE_MODULE_RESPONSE:
                        System.out.println("The response was generated directly by the " +
                                "caching module");
                        break;
                    case CACHE_MISS:
                        System.out.println("The response came from an upstream server");
                        break;
                    case VALIDATED:
                        System.out.println("The response was generated from the cache " +
                                "after validating the entry with the origin server");
                        break;
                }
            } finally {
                response.close();
            }
        }
    }

}