package com.demo.datasecurity.encryption.symmetric;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


/**
 * one-way hashing: deterministic , irreversible
 */
public class AESDemo {
    private static final String key = "aesEncryptionKey";
    private static final String initVector = "encryptionIntVec";

    public static String encrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String encryptWithoutIv(String value) {
        try {
            byte[] keyBytes = key.getBytes("UTF-8");
            SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /**
     CAUSE :
     Using RSA to encrypt a large file is not a good idea. You could for example generate a random AES key, encrypt it using RSA and store it in the output file, and then encrypt the file itself with AES, which is much faster and doesn't have any problem with large inputs. The decryption would read the encrypted AES key, decrypt it, and then decrypt the rest of the file with AES.
     " The RSA algorithm can only encrypt data that has a maximum byte length of the RSA key length in bits divided with eight minus eleven padding bytes, i.e. number of maximum bytes = key length in bits / 8 - 11 "
     SOLVE
     if key length 2048 maximum bytes data is 245 bytes
     if key length 1024 maximum bytes data is 117 bytes
     * @param args
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        System.out.println(encrypt("admin123"));
        System.out.println(encryptWithoutIv("admin123"));
        System.out.println(encrypt("admin123").equals("spmGUEdDV861UIyTRxOwcA=="));
        System.out.println(encryptWithoutIv("admin123").equals("spmGUEdDV861UIyTRxOwcA=="));

        // 2nd usage, generate symmetric key
        String plainText = "I am from mars and I am a big message, hash me first.";
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128); // The AES key size in number of bits
        SecretKey secKey = generator.generateKey();
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());


        // then encrypt the AES key


    }
}
