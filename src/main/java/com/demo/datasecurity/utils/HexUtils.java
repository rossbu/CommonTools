package com.demo.datasecurity.utils;

public class HexUtils {
    /**
     * Encodes a byte array to hex.
     */
    public static String encode(final byte[] bytes) {
        String chars = "0123456789abcdef";
        StringBuilder result = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            // convert to unsigned
            int val = b & 0xff;
            result.append(chars.charAt(val / 16));
            result.append(chars.charAt(val % 16));
        }
        return result.toString();
    }

    /**
     * Decodes a hex string to a byte array.
     */
    public static byte[] decode(String hex) {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException("Expected a string of even length");
        }
        int size = hex.length() / 2;
        byte[] result = new byte[size];
        for (int i = 0; i < size; i++) {
            int hi = Character.digit(hex.charAt(2 * i), 16);
            int lo = Character.digit(hex.charAt(2 * i + 1), 16);
            if ((hi == -1) || (lo == -1)) {
                throw new IllegalArgumentException("input is not hexadecimal");
            }
            result[i] = (byte) (16 * hi + lo);
        }
        return result;
    }
}
