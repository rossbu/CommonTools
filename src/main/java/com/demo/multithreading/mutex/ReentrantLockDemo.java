package com.demo.multithreading.mutex;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {

  private static final ReentrantLock lock = new ReentrantLock();

  public static void main(String[] args) {
    Thread thread1 = new Thread(() -> {
      if (lock.tryLock()) {
        try {
          System.out.println("Thread 1 acquired the lock");
          // Critical section
        } finally {
          lock.unlock();
          System.out.println("Thread 1 released the lock");
        }
      } else {
        System.out.println("Thread 1 couldn't acquire the lock");
      }
    });

    Thread thread2 = new Thread(() -> {
      if (lock.tryLock()) {
        try {
          System.out.println("Thread 2 acquired the lock");
          // Critical section
        } finally {
          lock.unlock();
          System.out.println("Thread 2 released the lock");
        }
      } else {
        System.out.println("Thread 2 couldn't acquire the lock");
      }
    });

    // Start the threads
    thread1.start();
    thread2.start();
  }
}
