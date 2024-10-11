package com.demo.thread;

public class InterruptDemo {

    private static Object sharedResource = new Object(); // Shared resource

    public static void main(String[] args) throws InterruptedException {

        // Create a sub-thread for the long-running task
        Thread subThread = new Thread(() -> {
            System.out.println("SubThread: Starting a long-running task...");

            synchronized (sharedResource) {
                try {
                    // Simulate long-running task
                    System.out.println("SubThread: Acquired the resource, working...");
                    Thread.sleep(5000);  // Mock long-running task
                } catch (InterruptedException e) {
                    // Re-interrupting to signal that the thread was interrupted
                    System.out.println("SubThread: Interrupted! Releasing resource...");
                    Thread.currentThread().interrupt();
                }
            }

            // After being interrupted, stop the work
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("SubThread: Stopping the long-running task.");
            }
        });


        // Simulate some delay before the newTask thread interrupts
        Thread.sleep(2000);  // Wait for 2 seconds to simulate urgent request

        // Create the newTask thread which needs the resource urgently
        Thread newTaskThread = new Thread(() -> {
            System.out.println("NewTask: This is an urgent task, now i am interrupting sub-thread...");
            subThread.interrupt();

            // Try to acquire the resource after interrupting the sub-thread
            synchronized (sharedResource) {
                System.out.println("NewTask: Acquired the resource, proceeding with urgent task...");
                // Do some urgent work
                try {
                    Thread.sleep(2000);  // Mock urgent work
                } catch (InterruptedException e) {
                    System.out.println("NewTask: Interrupted during urgent task.");
                }
            }
        });


        // Start the sub-thread
        subThread.start();
        // Start the newTask thread
        newTaskThread.start();

        // Wait for both threads to finish
        subThread.join();
        newTaskThread.join();

        System.out.println("MainThread: All tasks finished.");
    }
}
