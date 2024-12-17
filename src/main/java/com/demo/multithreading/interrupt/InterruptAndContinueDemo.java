package com.demo.multithreading.interrupt;

public class InterruptAndContinueDemo {

    private static final Object sharedResource = new Object(); // Shared resource
    private static boolean isNewTaskDone = false; // Flag to signal completion of newTaskThread

    public static void main(String[] args) throws InterruptedException {

        // Create a sub-thread for the long-running task
        Thread subThread = new Thread(() -> {
            synchronized (sharedResource) {
                System.out.println("SubThread: Starting a long-running task...");
                try {
                    System.out.println("SubThread: Acquired the resource, working...");
                    // Simulate long-running task
                    Thread.sleep(5000); // Mock long-running task
                } catch (InterruptedException e) {
                    System.out.println("SubThread: Interrupted by newTask! Now I waiting for newTask to finish...");

                    // Wait until newTaskThread finishes its work
                    while (!isNewTaskDone) {
                        try {
                            sharedResource.wait();  // SubThread waits for notification
                        } catch (InterruptedException waitException) {
                            Thread.currentThread().interrupt();  // Restore interrupt flag
                        }
                    }

                    // Continue subThread work after newTaskThread finishes
                    System.out.println("SubThread: NewTask finished, resuming work...");
                    // Resume long-running work or remaining tasks
                    try {
                        Thread.sleep(3000);  // Mock remaining work
                    } catch (InterruptedException finalException) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("SubThread: Finished its work.");
                }
            }
        });

        // Start the sub-thread
        subThread.start();

        // Simulate some delay before the newTask thread interrupts
        Thread.sleep(2000);  // Wait for 2 seconds to simulate an urgent request

        // Create the newTask thread which needs the resource urgently
        Thread newTaskThread = new Thread(() -> {
            System.out.println("NewTask: Urgent task started, interrupting sub-thread...");

            // Interrupt the sub-thread to take over the resource
            subThread.interrupt();

            // Acquire the shared resource and perform urgent work
            synchronized (sharedResource) {
                System.out.println("NewTask: Acquired the resource, performing urgent task...");
                try {
                    Thread.sleep(2000);  // Mock urgent work
                } catch (InterruptedException e) {
                    System.out.println("NewTask: Interrupted during urgent task.");
                }

                // Notify that newTask has completed its work
                System.out.println("NewTask: Urgent task completed, releasing resource...");
                isNewTaskDone = true;  // Set the flag to true to signal completion
                sharedResource.notify();  // Notify sub-thread that newTask is done
            }
        });

        // Start the newTask thread
        newTaskThread.start();

        // Wait for both threads to finish
        subThread.join();
        newTaskThread.join();

        System.out.println("MainThread: All tasks finished.");
    }
}
