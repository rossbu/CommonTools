package com.demo.exception;


/*

When you catch the InterruptedException and swallow it, you essentially prevent any higher-level methods/thread groups from noticing the interrupt. Which may cause problems.

By calling Thread.currentThread().interrupt(), you set the interrupt flag of the thread, so higher-level interrupt handlers will notice it and can handle it appropriately.

Java Concurrency in Practice discusses this in more detail in Chapter 7.1.3: Responding to Interruption. Its rule is:

Only code that implements a thread's interruption policy may swallow an interruption request.
General-purpose task and library code should NEVER swallow interruption requests.




w or w/o Thread.currentThread().interrupt() in catch block
When you catch an InterruptedException and do nothing ( swallow it ), the thread's interrupted flag is cleared.
when you are calling Thread.currentThread().interrupt(), you set the interrupted flag again so clients higher up the stack know the thread has been interrupted and can react accordingly
 */
public class InterruptedSleepingRunner implements Runnable {
    @Override
    public void run() {
        doAPseudoHeavyWeightJob();
    }

    private void doAPseudoHeavyWeightJob() {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            // You are kidding me
            System.out.println(i + " " + i * 2);
            // Let me sleep <evil grin>
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Thread interrupted\n Exiting...");
                break;
            } else {
                sleepBabySleep();
            }
        }
    }

    protected void sleepBabySleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}