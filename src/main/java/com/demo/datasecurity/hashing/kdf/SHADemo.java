package com.demo.datasecurity.hashing.kdf;

import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static com.demo.datasecurity.utils.BytesUtils.bytesToHex;
import static com.demo.datasecurity.utils.SaltUtils.getSalt;
import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_256;


/**
 https://www.novixys.com/blog/hmac-sha256-message-authentication-mac-java/  == MAC
 * when should we consider using hash in reality
 1. if you have to store the password in your system during registration, without revealing the 'original password' to the DBA and db team, then hashing the password and save value to the users table,
 2. if you have B 2 B integration,  aka service-to-service integration, then you can send the MAC with the secret key to the other party for verificiation.
 */
public class SHADemo {
    /**
        SHA-1 (and all other hashing algorithms) return binary data. That means that (in Java) they produce a byte[],
        so use base64 to encode teh byte[] and return.
     */
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String password = "admin1123";
        sha1(password); // SHA-1 is deprecated by all browsers.
        sha3(password);
        sha256(password);
        sha256WithSalting(password,getSalt());
    }


    /**
     * sha1 is deprecated by all browsers
     * @param data
     * @return
     */
    private static String sha1(String data) {
        byte[] hashedBytes = DigestUtils.sha1(data); // you can use sha1Hex directly, here just give it a try using bytes array.
        String base64Value = Base64.getEncoder().encodeToString(hashedBytes);
        System.out.println("sha1   hashed: " + base64Value);
        return base64Value;
    }

    public static String sha3(final String data) {
        String hashedPassword = DigestUtils.sha384Hex(data);
        System.out.println("sha3   hashed: " + hashedPassword);
        return hashedPassword;
    }

    private static String sha256(String data) {
        String hashedPassword = DigestUtils.sha256Hex(data);
        System.out.println("sha256 hashed: " + hashedPassword);
        return hashedPassword;
    }

    public static void sha256WithSalting(final String data, byte[] salt) throws NoSuchAlgorithmException {
        final MessageDigest messageDigest = MessageDigest.getInstance(SHA_256);
        messageDigest.update(salt);
        final byte[] encodedhash = messageDigest.digest(data.getBytes(StandardCharsets.UTF_8));
        System.out.println("sha256 hashed/Salted: " + bytesToHex(encodedhash));
    }





}
