package com.demo.multithreading.thread;

/**
 * Created by tbu on 6/26/2014.
 * Updated by tbu on 12/16/2024
 Runnable Advantages
 Flexibility:
 Runnable is more flexible as it allows the class to extend another class.
 Separation of Concerns:
 Using Runnable separates the task from the thread management, promoting better design.
 Reusability:
 Runnable can be reused by multiple threads, whereas extending Thread ties the task to a specific thread instance.
 */
public class ThreadAndRunnableSeparation {
    public static void main(String args[]) {
        System.out.println("Main thread starting.");
        ChildRunnable myRunnable = new ChildRunnable();
        // if you do myRunnable.run(); directly , it  won't start a new thread
        // you need to create an instance of Thread and pass the Runnable instance to it.
        // to showcase Thread and Runnable Separation of Concerns
        Thread newThread = new Thread(myRunnable);
        newThread.start();

        do {
            System.out.println("In main thread.");
            try {
                Thread.sleep(250);
            } catch (InterruptedException exc) {
                System.out.println("Main thread interrupted.");
            }
        } while (myRunnable.count != 5);

        System.out.println("Main thread ending.");
    }
}

class ChildRunnable
    implements Runnable {
    int count;

    ChildRunnable() {
        count = 0;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " - ChildRunnable starting - ");
        try {
            do {
                Thread.sleep(500);
                System.out.println(
                    Thread.currentThread().getName() + " - In ChildRunnable, count is " + count);
                count++;
            } while (count < 5);
        } catch (InterruptedException exc) {
            System.out.println("ChildRunnable interrupted.");
        }
        System.out.println("ChildRunnable terminating.");
    }
}
