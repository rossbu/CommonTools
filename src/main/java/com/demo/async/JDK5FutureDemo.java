package com.demo.async; /**
 * Created by tbu on 10/27/2016.
 */

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;


/**
 * 1 . JDK 6: Future
 * The future object is not the result, it is just a wrapper/placeholder/proyx/reference for the real result.
 * so When the caller invoke futureResult.get(); and the real result is not calculated yet,
 * this thread gets blocked until the result is provided by the worker
 */
@Service
public class JDK5FutureDemo extends FutureBase {


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        futureCaculate();
        callableFuture();
        scheduledExecutor();
        moreExecutorByGuava();
        existingExecutorByGuava();

    }

    private static void existingExecutorByGuava() {
        exitingExecutorService.submit(() -> {
            while (true) {
            }
        });
    }

    private static void moreExecutorByGuava() {
        guavaMoreExecutor.execute(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            executed.set(true);
        });
        executed.get();
    }

    private static void scheduledExecutor() {
        scheduledExecutorService.schedule(() -> {
            System.out.println("Hello World");
            return null;
        }, 500, TimeUnit.MILLISECONDS);
    }

    private static void callableFuture() {
        Set<Callable<String>> callables = new HashSet<Callable<String>>();

        callables.add(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "This is where I make the call to web service A, and put its results here";
            }
        });

        callables.add(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "This is where I make the call to web service B, and put its results here";
            }
        });

        callables.add(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "This is where I make the call to web service C, and put its results here";
            }
        });

        try {
            List<Future<String>> futures = newCachedThreadPoolExecutor.invokeAll(callables);
            for (Future<String> future : futures) {
                System.out.println(future.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static void futureCaculate() throws InterruptedException, ExecutionException {
        JDK5FutureDemo futureDemo = new JDK5FutureDemo();
        Future<Integer> future1 = futureDemo.calculate(10);
        Future<Integer> future2 = futureDemo.calculate(100);
        while (!(future1.isDone() && future2.isDone())) {
            System.out.println(
                    String.format(
                            "future1 is %s and future2 is %s",
                            future1.isDone() ? "done" : "not done",
                            future2.isDone() ? "done" : "not done"
                    )
            );
            Thread.sleep(300);
        }
        Integer result1 = future1.get();
        Integer result2 = future2.get();

        System.out.println(result1 + " and " + result2);
    }

    public Future<Integer> calculate(Integer input) {
        return singleThreadExecutor.submit(() -> {
            Thread.sleep(1000);
            return input * input;
        });
    }

    /**
     * this can replace agove anonymous class
     */
    class CallableTask implements Callable<String>
    {

        String message;

        public CallableTask(String message)
        {
            this.message = message;
        }

        @Override
        public String call() throws Exception
        {
            return message;
        }
    }
}
