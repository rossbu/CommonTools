package com.demo.datasecurity.encoding;

import org.apache.commons.codec.binary.Hex;

public class HexDemo {
    public static void main(String[] args) {
        String foo = "I am a string";
        byte[] bytes = foo.getBytes();
        System.out.println( Hex.encodeHexString( bytes ) );
    }
}
