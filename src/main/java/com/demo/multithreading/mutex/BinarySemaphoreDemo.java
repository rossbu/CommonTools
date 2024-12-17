package com.demo.multithreading.mutex;

import java.util.concurrent.Semaphore;

public class BinarySemaphoreDemo {

  private static final Semaphore semaphore = new Semaphore(1); // Binary semaphore

  public static void main(String[] args) {
    Thread thread1 = new Thread(() -> {
      try {
        semaphore.acquire();
        System.out.println("Thread 1 acquired the semaphore");
        Thread.sleep(1000);   // Critical section
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        semaphore.release();
        System.out.println("Thread 1 released the semaphore");
      }
    });

    Thread thread2 = new Thread(() -> {
      try {
        semaphore.acquire();
        System.out.println("Thread 2 acquired the semaphore");
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        semaphore.release();
        System.out.println("Thread 2 released the semaphore");
      }
    });

    // Start the threads
    thread1.start();
    thread2.start();
  }
}
