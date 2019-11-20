package com.demo.async;

import java.util.concurrent.*;


/**
 *
 How does the thread pool work

 Take this example. Starting thread pool size is 1, core pool size is 5, max pool size is 10 and the queue is 100.
 As requests come in, threads will be created up to 5 and then tasks will be added to the queue until it reaches 100.
 When the queue is full new threads will be created up to maxPoolSize.
 Once all the threads are in use and the queue is full tasks will be rejected.
 As the queue reduces, so does the number of active threads.
 */

public class ThreadPoolExecutorExample {

    public static void main (String[] args) {
        createAndRunPoolForQueue(new ArrayBlockingQueue<Runnable>(3), "Bounded");
        createAndRunPoolForQueue2Static();
//        createAndRunPoolForQueue(new LinkedBlockingDeque<>(), "Unbounded");
//        createAndRunPoolForQueue(new SynchronousQueue<Runnable>(), "Direct hand-off");
    }

    private static void createAndRunPoolForQueue (BlockingQueue<Runnable> queue, String msg) {
        System.out.println("---- " + msg + " queue instance = " + queue.getClass()+ " -------------");
        ThreadPoolExecutor e = new ThreadPoolExecutor(2, 5, Long.MAX_VALUE, TimeUnit.NANOSECONDS, queue);

        for (int i = 0; i < 10; i++) {
            try {
                e.execute(new Task());
            } catch (RejectedExecutionException ex) {
                System.out.println("Task rejected = " + (i + 1));
            }
            printStatus(i + 1, e);
        }

        e.shutdownNow();

        System.out.println("--------------------\n");
    }
    private static void createAndRunPoolForQueue2Static() {
        ThreadPoolExecutor e = new ThreadPoolExecutor(5, 10, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3));
        for (int i = 0; i < 10; i++) {
            try {
                e.execute(new Task());
            } catch (RejectedExecutionException ex) {
                System.out.println("Task rejected = " + (i + 1));
            }
            printStatus(i + 1, e);
        }

        e.shutdownNow();

        System.out.println("--------------------\n");
    }
    private static void printStatus (int taskSubmitted, ThreadPoolExecutor e) {
        StringBuilder s = new StringBuilder();
        s.append("poolSize = ")
                .append(e.getPoolSize())
                .append(", corePoolSize = ")
                .append(e.getCorePoolSize())
                .append(", queueSize = ")
                .append(e.getQueue()
                        .size())
                .append(", queueRemainingCapacity = ")
                .append(e.getQueue()
                        .remainingCapacity())
                .append(", maximumPoolSize = ")
                .append(e.getMaximumPoolSize())
                .append(", totalTasksSubmitted = ")
                .append(taskSubmitted);
        System.out.println(s.toString());
    }

    private static class Task implements Runnable {
        @Override
        public void run () {
            while (true) {
                try {
                    Thread.sleep(1000000); // 1000seconds
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
}