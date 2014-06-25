package com.tools.jdk.ext;

/**
 * Created by tbu on 6/25/2014.
 */
public class ShutdownHookDemo {
    // a class that extends thread that is to be called when program is exiting
    static class Message extends Thread {

        public void run() {
            System.out.println("Bye." + Thread.currentThread().getName());
        }
    }

    public static void main(String[] args) {
        try {
            Message p = new Message();

            // register Message as shutdown hook
            Runtime.getRuntime().addShutdownHook(p);

            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    System.out.println("bye bye!" + Thread.currentThread().getName());
                }
            });
            // print the state of the program
            System.out.println("Program is starting..." + Thread.currentThread().getName());

            // cause thread to sleep for 3 seconds
            System.out.println("Waiting for 3 seconds...");
            Thread.sleep(3000);

            // remove the hook
//            Runtime.getRuntime().removeShutdownHook(p);
            // print that the program is closing
            System.out.println("Program is closing...");
            System.gc();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
