package com.demo.encryption.rsa;
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
        RSAKeyPairGenerator rsaKeyPairGenerator = new RSAKeyPairGenerator();

        try {

            String privateKey = Base64.getEncoder().encodeToString(rsaKeyPairGenerator.getPrivateKey().getEncoded());  // never expose to the web.
            String publicKey = Base64.getEncoder().encodeToString(rsaKeyPairGenerator.getPublicKey().getEncoded());  // public key = 'SAFE' , everyone can download it.
            System.out.println("private key : " + privateKey);
            System.out.println("public key  : " + publicKey + "\n");


//            String cipherType = "RSA/ECB/PKCS1Padding";
            String cipherType = "RSA/ECB/OAEPWITHSHA1ANDMGF1PADDING";

            String alienMessage = "I am actually from Mars, please come to Mars on 2020:2020";
            System.out.println("Client Message : " + alienMessage);
            // use encryptedMessage + publicKey + cipherType ( 3 factors) to forcely break it in reverse ( when you are hacking without private key ).
            String encryptedMessage = Base64.getEncoder().encodeToString(encrypt(alienMessage,publicKey, cipherType));
            System.out.println("Client Sending (encrpted by RSA): " +encryptedMessage);


            // server side uses encryptedMessage + publicKey + privateKey + cipherType to decrypt it and get the original message. ( 4 factors)
            String decryptedString = decrypt(encryptedMessage, privateKey, cipherType);
            System.out.println("Server Receiving : " + decryptedString);



            // --- we need a key pair to test encryption/decryption
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024); // speedy generation, but not secure anymore
            KeyPair kp = kpg.generateKeyPair();
            RSAPublicKey pubkey = (RSAPublicKey) kp.getPublic();
            RSAPrivateKey privkey = (RSAPrivateKey) kp.getPrivate();

            // --- encrypt given algorithm string with public key
            Cipher oaepFromAlgo = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
            oaepFromAlgo.init(Cipher.ENCRYPT_MODE, pubkey);
            byte[] ct = oaepFromAlgo.doFinal("1000.1234".getBytes(StandardCharsets.UTF_8));

            // --- decrypt given OAEPParameterSpec  with private key
            Cipher oaepFromInit = Cipher.getInstance("RSA/ECB/OAEPPadding");
            OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-1"), PSource.PSpecified.DEFAULT);
            oaepFromInit.init(Cipher.DECRYPT_MODE, privkey, oaepParams);
            byte[] pt = oaepFromInit.doFinal(ct);
            System.out.println(new String(pt, StandardCharsets.UTF_8));




        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            System.err.println(e.getMessage());
        }
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