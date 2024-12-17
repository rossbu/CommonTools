package com.demo.multithreading.mutex;

import java.util.concurrent.atomic.AtomicBoolean;

public class TwoThreadsMutexes {
    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        AtomicBoolean task1Completed = new AtomicBoolean(false);

        // Task 1
        Thread thread1 = new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("Task 1 executed.");
                synchronized (lock) {
                    task1Completed.set(true);
                    lock.notify();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });


        // Task 2
        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(950);
                System.out.println("Task 2 executed.");
                synchronized (lock) {
                    while (!task1Completed.get()) {
                        lock.wait();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        thread1.start();
        thread2.start();

        // Wait for both threads to complete
        thread1.join();
        thread2.join();

        System.out.println("Both tasks have completed.");
    }
}
