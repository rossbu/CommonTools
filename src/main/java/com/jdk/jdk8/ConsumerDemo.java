package com.jdk.jdk8;

import java.util.function.Consumer;

public class ConsumerDemo {
    static Consumer<String> c = System.out::println;
    public static void main(String[] args) {
        c.accept("hello consumer");
    }
}
