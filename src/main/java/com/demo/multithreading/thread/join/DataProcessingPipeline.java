package com.demo.multithreading.thread.join;


public class DataProcessingPipeline {

  public static void main(String[] args) throws InterruptedException {
    Thread fetchDataThread = new Thread(() -> {
      System.out.println("Fetching data from remote server...");
      try {
        Thread.sleep(5000); // Simulate time taken to fetch data
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println("Data fetched successfully.");
    });

    Thread processDataThread = new Thread(() -> {
      System.out.println("Processing data...");
      try {
        Thread.sleep(3000); // Simulate time taken to process data
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println("Data processed successfully.");
    });

    Thread saveDataThread = new Thread(() -> {
      System.out.println("Saving data to database...");
      try {
        Thread.sleep(1000); // Simulate time taken to save data
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println("Data saved successfully.");
    });

    // Start the threads
    fetchDataThread.start();
    fetchDataThread.join();
    processDataThread.start();
    processDataThread.join();
    saveDataThread.start();
    saveDataThread.join();

    System.out.println("Data processing pipeline completed.");
  }

}
