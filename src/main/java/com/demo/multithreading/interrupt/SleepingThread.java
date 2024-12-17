package com.demo.multithreading.interrupt;


/**
 To showcase the join() function, we can add some print statements to indicate
 when the main thread is waiting for each SleepingThread to complete and when each SleepingThread has finished



 */
public class SleepingThread extends Thread {

    private int countDown = 5;
    private static int threadCount = 0;

    public SleepingThread() {
        super("" + ++threadCount);
        start();
    }

    @Override
    public String toString() {
        return "T" + getName() + ": " + countDown;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(this);
            if (--countDown == 0) {
                System.out.println(getName() + " has finished.");
                return;
            }
            try {
                sleep(100); // sleep is Pause actually
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            SleepingThread t = new SleepingThread();
            System.out.println("Main thread waiting for " + t.getName() + " to finish.");
            t.join();
            System.out.println(t.getName() + " has finished. Main thread resumes.");
        }
    }
}
