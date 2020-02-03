package com.demo.datasecurity.hashing;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import static com.demo.datasecurity.utils.SaltUtils.getRandomSalt;

public class MACDemo {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        String password = "admin1123";
        getRandomSalt();
    }


    public static SecretKey generateSecretKeyByAPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String salt = getRandomSalt();
        char[] passwordChar = password.toCharArray();
        String algo = "PBKDF2WITHHMACSHA256"; // PBKDF2 - SHA256
        SecretKeyFactory factory = SecretKeyFactory.getInstance(algo);
        KeySpec spec = new PBEKeySpec(passwordChar, salt.getBytes(Charset.defaultCharset()), 10000, 128);
        SecretKey secretKey = factory.generateSecret(spec);
        return secretKey;
    }

    /**
     * Once the MAC is obtained, you can send it to the other party along with the file for verification.
     * @param password
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static Mac generateMAC(String password) throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException {
        SecretKey secretKey = generateSecretKeyByAPassword(password);
        String algo = "PBEWITHHMACSHA256";
        Mac mac = Mac.getInstance(algo);
        mac.init(secretKey);
        return mac;
    }
}
