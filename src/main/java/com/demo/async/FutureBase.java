package com.demo.async;

import com.google.common.util.concurrent.MoreExecutors;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;


/**
  check link for details :   https://www.baeldung.com/thread-pool-java-and-guava
 */
public class FutureBase {

    RestTemplate restTemplate = new RestTemplate();

    static AtomicBoolean executed = new AtomicBoolean();
    /*
     Executors, Executor and ExecutorService
     1. The Executor and ExecutorService interfaces are used to work with different thread pool implementations in Java.
     2. you should keep your code decoupled from the actual implementation of the thread pool


     Notes:
     The Executor interface has a single execute method to submit Runnable instances for execution
     The ExecutorService interface contains a large number of methods for controlling the progress of the tasks and managing the termination of the service.

     ScheduledThreadPoolExecutor extends the ThreadPoolExecutor

     Runnable‘s single method does not throw an exception and does not return value.
     Callable interface may be more convenient, as it allows to throw an exception and return a value.
     */

    static ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    static ExecutorService newCachedThreadPoolExecutor = Executors.newCachedThreadPool();
    static Executor newFixedThreadPoolExecutor = Executors.newFixedThreadPool(2);
    static ExecutorService newFixedThreadExecutorService = Executors.newFixedThreadPool(10);
    static ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
    static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

    static ExecutorService fixedCustomizedThreadPoolExecutor = Executors.newFixedThreadPool(3, new ThreadFactory() {
        int count = 1;
        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "custom-executor-" + count++);
        }
    });

    /*
        3rd party Executor impl
        MoreExecutors: The instance returned by the directExecutor() method is actually a static singleton,
        so using this method does not provide any overhead on object creation at all.
     */

    static Executor guavaMoreExecutor = MoreExecutors.directExecutor();
    static ExecutorService exitingExecutorService = MoreExecutors.getExitingExecutorService(threadPoolExecutor, 100, TimeUnit.MILLISECONDS);
}
