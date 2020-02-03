package com.demo.datasecurity.hashing;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;

public class HMACDemo {

    public static SecretKey generateHMACKey() throws Exception {
        final KeyGenerator keyGen;
        try {
            keyGen = KeyGenerator.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("HMAC KeyGeneration should be available");
        }
        keyGen.init(128);
        SecretKey key = keyGen.generateKey();
        return key;
    }

    public static void writeToFile(SecretKey key, String filename)
            throws IOException {
        // file handling probably should be in a separate class
        Files.write((new File(filename)).toPath(), key.getEncoded());
    }

    public static RSAPublicKey readRSAPublicKey(String filename) throws IOException, InvalidKeySpecException {
        try (PemReader reader = new PemReader(new FileReader(filename))) {
            PemObject pemObject = reader.readPemObject();
            KeyFactory kf;
            try {
                kf = KeyFactory.getInstance("RSA");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("RSA key factory not available", e);
            }
            KeySpec keySpec = new X509EncodedKeySpec(pemObject.getContent());
            try {
                return (RSAPublicKey) kf.generatePublic(keySpec);
            } catch (ClassCastException e) {
                throw new InvalidKeySpecException("That's no RSA key", e);
            }
        }
    }

    public static byte[] wrapKey(Key key, RSAPublicKey wrappingKey) throws InvalidKeyException, IllegalBlockSizeException {
        Cipher rsaWrapper;
        try {
            rsaWrapper = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
            rsaWrapper.init(Cipher.WRAP_MODE, wrappingKey);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            throw new RuntimeException("RSA OAEP should be available for RSA public key", e);
        }
        return rsaWrapper.wrap(key);
    }

    public static void main(String[] args) throws Exception {

        // HMAC-SHA256 demo
        String hashResult = hmacSHA256_simpleKDF("admin123", "i am a message");
        hmacSHA256_complicatedKDF_withSalt();


        // we need an RSA PEM key first I guess :)
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024, new SecureRandom());
        KeyPair kp = keyPairGenerator.generateKeyPair();
        String publicKeyFilename = "rsa_pub.pem";
        try (PemWriter pemWriter = new PemWriter(new FileWriter(publicKeyFilename))) {
            pemWriter.writeObject(new PemObject("PUBLIC KEY", kp.getPublic().getEncoded()));
        }

        RSAPublicKey wrappingRSAPublicKey = readRSAPublicKey(publicKeyFilename);
        SecretKey hmacKey = generateHMACKey();
        byte[] wrappedKey = wrapKey(hmacKey, wrappingRSAPublicKey);
        System.out.println(Base64.getEncoder().encodeToString(wrappedKey));
    }


    /**
     As more complicated KDF function,
     you can derive a password by calculating HMAC(salt, msg, SHA256) using some random value called "salt",
     which is stored along with the derived key and used later to derive the same key again from the password.
     */
    private static void hmacSHA256_complicatedKDF_withSalt() {

    }

    /**
     * As very simple KDF function, we can use SHA256: just hash the password.
     * But Don't do this in real production software, because it is insecure. Simple hashes are vulnerable to dictionary attacks.
     *
     * formula: hmac(message, key, hash_function)  such as hmacSHA256(message, key);
     * @return
     */
    private static String hmacSHA256_simpleKDF(String message,String secret) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(message.getBytes());
            String hash = Base64.getEncoder().encodeToString(bytes);
            System.out.println("hmacSHA256 hash: "+ hash );
            return hash;
        }
        catch (Exception e){
            System.out.println("Error");
        }
        return null;
    }
}