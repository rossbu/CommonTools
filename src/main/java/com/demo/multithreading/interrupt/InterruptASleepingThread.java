package com.demo.multithreading.interrupt;

public class InterruptASleepingThread {
    public static void main(String[] args)
            throws InterruptedException
    {
        Task task1 = new Task();
        task1.start();
        Task task2 = new Task();
        task2.start();
        task1.interrupt();        // main thread calls interrupt() method on child thread


        System.out.println("Main thread execution completes");
    }

    static class Task extends Thread {
        public void run()
        {
            try {
                for (int i = 0; i < 5; i++) {
                    System.out.println("Child task executing: " + Thread.currentThread().getName());

                    // Here current thread goes to sleeping state Another thread gets the chance to execute
                    Thread.sleep(1000);
                }
            }
            catch (InterruptedException e) {
                System.out.println("InterruptedException occur");
            }
        }
    }
}
