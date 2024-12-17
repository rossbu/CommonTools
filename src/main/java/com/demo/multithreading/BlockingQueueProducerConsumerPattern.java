package com.demo.multithreading;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BlockingQueueProducerConsumerPattern {

    public static void main(String args[]){

        //Creating shared object
        BlockingQueue<Integer> sharedQueue = new LinkedBlockingQueue<>();

        //Creating Producer and Consumer Thread
        Thread producerThread = new Thread(new Producer(sharedQueue));
        Thread consumerThread1 = new Thread(new Consumer(sharedQueue, "consumer1"));
        Thread consumerThread2 = new Thread(new Consumer(sharedQueue, "consumer2"));

        //Starting producer and Consumer thread
        producerThread.start();
        consumerThread1.start();
        consumerThread2.start();
    }

}


class Producer implements Runnable {

    private final BlockingQueue<Integer> sharedQueue;
    public Producer(BlockingQueue<Integer> sharedQueue) {
        this.sharedQueue = sharedQueue;
    }

    @Override
    public void run() {
        for(int i=0; i<100; i++){
            try {
                System.out.println("Produced: " + i);
                sharedQueue.put(i);
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}

class Consumer implements Runnable{

    private final String consumerName;
    private final BlockingQueue<Integer> sharedQueue;
    private int consumedCount = 0;

    public Consumer(BlockingQueue<Integer> sharedQueue, String name) {
        this.sharedQueue = sharedQueue;
        this.consumerName = name;
    }

    @Override
    public void run() {
        while(true){
            try {
                Integer take = sharedQueue.take();
                consumedCount++;
                System.out.println(consumerName + " - Consumed: " + take);
            } catch (InterruptedException ex) {
                Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
