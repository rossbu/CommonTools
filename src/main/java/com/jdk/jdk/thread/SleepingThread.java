package com.jdk.jdk.thread;

/**
 * Created by tbu on 6/26/2014.
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
        return "#" + getName() + ": " + countDown;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(this);
            if (--countDown == 0) {
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
            new SleepingThread().join();
        }
    }
}