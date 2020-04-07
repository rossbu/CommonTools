package com.jdk.jdk13;


public class TextBlockDemo {
    public static void main(String[] args) {
        String json = """
        test me \s
        test me again\s
        haha
        """;
        System.out.println(json);
    }
}
