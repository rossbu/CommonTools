package com.demo.datasecurity.encoding;

import java.util.Base64;


/**
 * Base64 always increases data size by 33% (plus some potential padding by the = characters that are sometimes added at the end of the base64 output)
 *
 * read 3 bytes and output 4 : encode
 * read 4 bytes and output 3 : decode.
 */
public class Base64Demo {
    public static void main(String[] args) {
        String foo = "I am a string";
        byte[] bytes = foo.getBytes();
        String base64Value = Base64.getEncoder().encodeToString(bytes);
        System.out.println(base64Value);
        int fooLength = foo.length();
        int based64Length = base64Value.length();
        System.out.println("Original length is : " + fooLength);
        System.out.println("after base64 the length is : " + based64Length);
        System.out.println("size increased by base64 is : " +calculatePercentage(based64Length - fooLength, based64Length));
    }

    public static double calculatePercentage(double obtained, double total) {
        return obtained * 100 / total;
    }
}
