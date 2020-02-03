package com.demo.datasecurity.encryption.asymmeric;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * ref: https://github.com/only2dhir/rsaencryption/blob/master/src/main/java/com/devglan/rsa/RSAUtil.java
 * https://www.devglan.com/java8/rsa-encryption-decryption-java
 *
 1. RSA/ECB/PKCS1Padding: Invoke rsa.Encrypt(data, RSAEncryptionPadding.Pkcs1)
 2. RSA/ECB/OAEPPadding: Invoke rsa.Encrypt(data, RSAEncryptionPadding.OaepSHA1)

 */
public class RSADemo {

    public static void main(String[] args) throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException {


        try {
            rsaEncryptDecrypt0();
            rsaEncryptDecrypt1();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            System.err.println(e.getMessage());
        }
    }

    protected static void rsaEncryptDecrypt0() throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        RSAKeyPairGenerator rsaKeyPairGenerator = new RSAKeyPairGenerator();
        String privateKeyInBase64 = Base64.getEncoder().encodeToString(rsaKeyPairGenerator.getPrivateKey().getEncoded());  // never expose to the web.
        String publicKeyInBase64 = Base64.getEncoder().encodeToString(rsaKeyPairGenerator.getPublicKey().getEncoded());  // public key = 'SAFE' , everyone can download it. provided by earth
        System.out.println("private key : " + privateKeyInBase64);
        System.out.println("public key  : " + publicKeyInBase64 + "\n");
//
        String cipherType = "RSA/ECB/OAEPWITHSHA1ANDMGF1PADDING";  // provided by earch  try "RSA/ECB/PKCS1Padding";
        String alienMessage = "I am from Mars save us"; // alien message ( no more than 22 byte
        byte[] encryptedBytes = encrypt(alienMessage, publicKeyInBase64, cipherType); //encryptedMessage + publicKeyInBase64 + cipherType ( 3 factors)
        String base64EncodedMessage = Base64.getEncoder().encodeToString(encryptedBytes);
        System.out.println("Alien ( from mars) Sending : " +base64EncodedMessage);


        // uses encryptedMessage + privateKeyInBase64 + cipherType to decrypt it and get the original message. ( 4 factors)
        String decryptedMessage = decrypt(base64EncodedMessage, privateKeyInBase64, cipherType);
        System.out.println("Human ( on earth)  Receiving : " + decryptedMessage);
        System.out.println("\n");
    }

    protected static void rsaEncryptDecrypt1() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        // --- we need a key pair to test encryption/decryption
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024); // speedy generation, but not secure anymore
        KeyPair kp = kpg.generateKeyPair();
        RSAPublicKey pubkey = (RSAPublicKey) kp.getPublic();
        RSAPrivateKey privkey = (RSAPrivateKey) kp.getPrivate();

        // --- encrypt given algorithm string with public key
        Cipher oaepFromAlgo = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
        oaepFromAlgo.init(Cipher.ENCRYPT_MODE, pubkey);
        String alienMessage = "1000.1234";
        System.out.println("Alien is sending message : " + alienMessage + " | cipher RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING | pubkey"+pubkey.getEncoded());
        byte[] ct = oaepFromAlgo.doFinal(alienMessage.getBytes(StandardCharsets.UTF_8));

        // --- decrypt given OAEPParameterSpec  with private key
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
        OAEPParameterSpec oaepParameterSpec = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-1"), PSource.PSpecified.DEFAULT);
        cipher.init(Cipher.DECRYPT_MODE, privkey, oaepParameterSpec);
        byte[] pt = cipher.doFinal(ct);
        System.out.println("Human receives : " + new String(pt, StandardCharsets.UTF_8));

    }

    public static PublicKey getPublicKey(String base64PublicKey){
        PublicKey publicKey = null;
        try{
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    /**
     *
     * @param base64PrivateKey a private key string.
     * @return a PrivateKey object
     */
    public static PrivateKey getPrivateKey(String base64PrivateKey){
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    public static byte[] encrypt(String data, String publicKey, String cipherType) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance(cipherType);
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
        return cipher.doFinal(data.getBytes());
    }

    public static String decrypt(byte[] data, PrivateKey privateKey, String cipherType) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(cipherType);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(data));
    }

    public static String decrypt(String data, String base64PrivateKey, String cipherType) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return decrypt(Base64.getDecoder().decode(data.getBytes()), getPrivateKey(base64PrivateKey), cipherType);
    }

}