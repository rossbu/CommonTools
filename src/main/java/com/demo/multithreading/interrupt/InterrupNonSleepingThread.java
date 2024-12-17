package com.demo.multithreading.interrupt;

public class InterrupNonSleepingThread extends Thread {

  public void run() {
    for (int i = 0; i < 5; i++) {
      System.out.println(i);
    }

      if (isInterrupted()) {
          System.out.println("Thread is interrupted");
      } else {
          System.out.println("Thread is not interrupted");
      }
  }

  public static void main(String args[]) {
    InterrupNonSleepingThread t1 = new InterrupNonSleepingThread();
    t1.start();
    t1.interrupt();
    //  interrupt only sets the interrupted flag to true
    // Since the thread is not in a sleeping or waiting state
  }
}
