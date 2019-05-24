package com.example.jdk.thread;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Java program to demonstrate How to use CountDownLatch in Java. CountDownLatch is
 * useful if you want to start main processing thread once its dependency is completed
 * as illustrated in this CountDownLatch Example
 *
 *
 1) You can not reuse CountDownLatch once count is reaches to zero, this is the main difference between CountDownLatch and CyclicBarrier,
 which is frequently asked in core Java interviews and multi-threading  interviews.

 2) Main Thread wait on Latch by calling CountDownLatch.await() method
 while other thread calls CountDownLatch.countDown() to inform that they have completed.


 Read more: https://javarevisited.blogspot.com/2012/07/countdownlatch-example-in-java.html#ixzz5os4Rm8RS
 *
 * @author Javin Paul
 */
public class CountDownLatchDemo {

    public static void main(String args[]) {
        int numOfGates = 3 ;
        final CountDownLatch latch = new CountDownLatch(numOfGates);
        Thread cacheService = new Thread(new Service("CacheService", 1000, latch));
        Thread alertService = new Thread(new Service("AlertService", 1000, latch));
        Thread validationService = new Thread(new Service("ValidationService", 1000, latch));

        cacheService.start(); //separate thread will initialize CacheService
        alertService.start(); //another thread for AlertService initialization
        validationService.start();

        // application should not start processing any thread until all service is up
        // and ready to do there job.
        // Countdown latch is idle choice here, main thread will start with count 3
        // and wait until count reaches zero. each thread once up and read will do
        // a count down. this will ensure that main thread is not started processing
        // until all services is up.

        //count is 3 since we have 3 Threads (Services)

        try {
            latch.await();  //main thread is waiting on CountDownLatch to finish
            System.out.println("All services are up, Application is starting now");
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

    }

}

/**
 * Service class which will be executed by Thread using CountDownLatch synchronizer.
 */
class Service implements Runnable {
    private final String name;
    private final int timeToStart;
    private final CountDownLatch latch;

    public Service(String name, int timeToStart, CountDownLatch latch) {
        this.name = name;
        this.timeToStart = timeToStart;
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(timeToStart);
        } catch (InterruptedException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(name + " is Up");
        latch.countDown(); //reduce count of CountDownLatch by 1
    }

}