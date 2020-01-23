package com.demo.encryption.hashing;
import org.apache.commons.codec.binary.Hex;

public class MD5Demo {

    public static void main(String[] args) {
        String foo = "I am a string";
        byte[] bytes = foo.getBytes();
        System.out.println( Hex.encodeHexString( bytes ) );
    }
}
