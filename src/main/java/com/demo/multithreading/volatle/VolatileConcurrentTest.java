package com.demo.multithreading.volatle;

public class VolatileConcurrentTest implements Runnable {

    // synchronized keyword is used to acquire a lock for an object.
    private static final Object lock = new Object();

    // volatile keyword is used to indicate that a variable's value will be modified by different threads.
    private static int counter = 0;
    private static volatile int counter1 = 0;
    private volatile int counter2 = 0;
    private int counter3 = 0;

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            concurrentMethodWrong();
        }
    }


    public void concurrentMethodWrong() {
        counter = counter + 1;
        System.out.println( Thread.currentThread().getName() +" « Static :: "+ counter);
        sleepThread( 1/4 );

        counter1 = counter1 + 1;
        System.out.println( Thread.currentThread().getName() +"\t « Static Volatile :: "+ counter1);
        sleepThread( 1/4 );

        synchronized (lock) {
            counter2 = counter2 + 1;
        }
        System.out.println( Thread.currentThread().getName() +"\t « Instance Volatile :: "+ counter2);
        sleepThread( 1/4 );

        counter3 = counter3 + 1;
        sleepThread( 1/4 );
        System.out.println( Thread.currentThread().getName() +"\t\t\t\t\t « Instance :: "+ counter3);
    }

    public static void main(String[] args) throws InterruptedException {
        VolatileConcurrentTest volatileTest = new VolatileConcurrentTest();
        Thread t1 = new Thread( volatileTest );
        t1.start();

        Thread t2 = new Thread( volatileTest );
        t2.start();

        Thread t3 = new Thread( volatileTest );
        t3.start();

        Thread t4 = new Thread( volatileTest );
        t4.start();

        Thread.sleep( 10 );

        Thread optimization = new Thread() {
            @Override public void run() {
                System.out.println("Thread Start.");

                Integer appendingVal = volatileTest.counter2 + volatileTest.counter2 + volatileTest.counter2;

                System.out.println("End of Thread." + appendingVal);
            }
        };
        optimization.start();
    }

    public void sleepThread( long sec ) {
        try {
            Thread.sleep( sec * 1000 );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
