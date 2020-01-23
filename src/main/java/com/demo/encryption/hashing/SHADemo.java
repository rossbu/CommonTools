package com.demo.encryption.hashing;

import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import static javax.xml.crypto.dsig.DigestMethod.SHA3_256;
import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_256;

public class SHADemo {

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
    /*
        SHA-1 (and all other hashing algorithms) return binary data. That means that (in Java) they produce a byte[],
        so use base64 to encode teh byte[] and return.
     */
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String password = "admin1123";

        // start sha[X] the password
        sha1(password);
        sha3(password);
        sha256(password);
        sha256WithJavaMessageDigest(password);
    }

    private static void sha256(String password) {
        String result = DigestUtils.sha256Hex(password);
        System.out.println("sha256 hashed: " + result);
    }

    public static void sha256WithJavaMessageDigest(final String message) throws NoSuchAlgorithmException {
        final MessageDigest messageDigest = MessageDigest.getInstance(SHA_256);

        // Generate the random salt， not necessary,
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        messageDigest.update(salt);


        final byte[] encodedhash = messageDigest.digest(message.getBytes(StandardCharsets.UTF_8));
        System.out.println("sha256 hashed: " + bytesToHex(encodedhash));
    }

    public static void sha3(final String password) {
        byte[] bytes = DigestUtils.sha384(password);
        System.out.println("sha3   hashed: " + Base64.getEncoder().encodeToString(bytes));
    }
    private static void sha1(String password) {
        byte[] stringInbytes = password.getBytes(Charset.forName("UTF-8"));
        byte[] hashedBytes = org.apache.commons.codec.digest.DigestUtils.sha1(stringInbytes);
        String base64Value = Base64.getEncoder().encodeToString(hashedBytes);
        System.out.println("sha1   hashed: " + base64Value);
    }
}
