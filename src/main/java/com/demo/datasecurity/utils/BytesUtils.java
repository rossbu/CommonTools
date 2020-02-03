package com.demo.datasecurity.utils;

import org.apache.commons.codec.binary.Base64;

import java.security.GeneralSecurityException;

public class BytesUtils {
    /**
     https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
     */
    public static String bytesToHex2(byte[] bytes) {
        char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
    public static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (byte h : hash) {
            String hex = Integer.toHexString(0xff & h);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String bytesToBase64(byte[] input){
        return Base64.encodeBase64String(input);
    }
    /**
     * Returns the concatenation of the input arrays in a single array. For example, {@code concat(new
     * byte[] {a, b}, new byte[] {}, new byte[] {c}} returns the array {@code {a, b, c}}.
     *
     * @return a single array containing all the values from the source arrays, in order
     */
    public static byte[] concat(byte[]... chunks) throws GeneralSecurityException {
        int length = 0;
        for (byte[] chunk : chunks) {
            if (length > Integer.MAX_VALUE - chunk.length) {
                throw new GeneralSecurityException("exceeded size limit");
            }
            length += chunk.length;
        }
        byte[] res = new byte[length];
        int pos = 0;
        for (byte[] chunk : chunks) {
            System.arraycopy(chunk, 0, res, pos, chunk.length);
            pos += chunk.length;
        }
        return res;
    }

}
