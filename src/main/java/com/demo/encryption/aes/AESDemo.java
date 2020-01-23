package com.demo.encryption.aes;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


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


    public static void main(String[] args) {
        System.out.println(encrypt("admin123"));
        System.out.println(encryptWithoutIv("admin123"));
        System.out.println(encrypt("admin123").equals("spmGUEdDV861UIyTRxOwcA=="));
        System.out.println(encryptWithoutIv("admin123").equals("spmGUEdDV861UIyTRxOwcA=="));
    }
}
