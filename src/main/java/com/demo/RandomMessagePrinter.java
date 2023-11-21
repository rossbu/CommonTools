package com.demo;


import java.util.Random;

public class RandomMessagePrinter {
    public static void main(String[] args) {
        printRandomMessage(new Random(),1);

//        @LogProbibility(1) someMethod();
    }

    public static void printRandomMessage(Random random, int probability) {
        if (random.nextInt(100) < probability) {
            someMethod();
        }
    }

    private static void someMethod() {
        System.out.println("Printing a random message!");
    }
}