package com.demo.multithreading.thread.countdownlatch;

import java.util.concurrent.CountDownLatch;

public class DataProcessingPipeline {

  public static void main(String[] args) throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(3);

    Thread fetchDataThread = new Thread(() -> {
      System.out.println("Fetching data from remote server...");
      try {
        Thread.sleep(2000); // Simulate time taken to fetch data
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println("Data fetched successfully.");
      latch.countDown();
    });

    Thread processDataThread = new Thread(() -> {
      System.out.println("Processing data...");
      try {
        Thread.sleep(3000); // Simulate time taken to process data
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println("Data processed successfully.");
      latch.countDown();
    });

    Thread saveDataThread = new Thread(() -> {
      System.out.println("Saving data to database...");
      try {
        Thread.sleep(1000); // Simulate time taken to save data
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println("Data saved successfully.");
      latch.countDown();
    });

    // Start the threads
    fetchDataThread.start();
    processDataThread.start();
    saveDataThread.start();

    // Wait for all threads to complete
    latch.await();

    System.out.println("Data processing pipeline completed.");
  }
}
