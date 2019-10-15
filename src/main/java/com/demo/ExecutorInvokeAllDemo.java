package com.demo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class ExecutorInvokeAllDemo
{

    public static void main(String[] args) throws InterruptedException, ExecutionException
    {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Set<Callable<String>> callables = new HashSet<Callable<String>>();

        callables.add(new CallableTask("Hello 1"));
        callables.add(new CallableTask("Hello 2"));
        callables.add(new CallableTask("Hello 3"));

        List<Future<String>> futures = executorService.invokeAll(callables);

        for (Future<String> future : futures)
        {
            System.out.println("future.get = " + future.get());
        }
        executorService.shutdown();
    }
}

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