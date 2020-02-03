package com.demo.datasecurity.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class SaltUtils {

    public static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    /**
     *
     * @return  base64 encoded salt ( not Base16 Hex encoded )
     */
    public static String getRandomSalt() {
        Base64.Encoder encoder = Base64.getEncoder();
        SecureRandom srandom = new SecureRandom();
        System.out.println("default algo/prng is : " + srandom.getAlgorithm());
        byte[] salt = new byte[8];
        srandom.nextBytes(salt);
        String randomSalt = encoder.encodeToString(salt);
        System.out.println("Salt: " + randomSalt);
        return randomSalt;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        getRandomSalt();
        getSalt();
    }
}
