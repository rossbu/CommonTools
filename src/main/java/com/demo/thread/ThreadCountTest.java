package com.demo.thread;

public class ThreadCountTest {
    public static void main(String[] args) {
        int maxThreads = Integer.MAX_VALUE; // Set a very high number initially, theoretically, it should be Integer.MAX_VALUE

        try {
            for (int i = 1; i <= maxThreads; i++) {
                Thread newThread = new Thread(new MyRunnable(i));
                newThread.start();
                Thread.sleep(10); // Adjust this delay as needed
            }
        } catch (OutOfMemoryError e) {
            System.out.println("Out of memory error occurred.");
            System.out.println("Maximum threads the machine can handle: " + Thread.activeCount());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class MyRunnable implements Runnable {
        private int threadNumber;

        public MyRunnable(int threadNumber) {
            this.threadNumber = threadNumber;
        }

        @Override
        public void run() {
            long threadId = Thread.currentThread().getId();
            String threadName = Thread.currentThread().getName();
            int threadPriority = Thread.currentThread().getPriority();
//            System.out.println("Thread " + threadNumber + " created by NAME: " + threadName + " (ID: " + threadId + ")");
            System.out.println("Thread " + threadNumber + " created with priority: " + threadPriority + " (Thread ID: " + threadId + ")");


            while (true) {
                try {
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}