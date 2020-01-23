package com.demo.encryption;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class Encryptor {
    static  String encKeyString =  "1234567890123456";
    static String message = "This is a secret message";

    static  String  certificateString = "-----BEGIN CERTIFICATE-----\n" +
            "MIICVjCCAb8CAg37MA0GCSqGSIb3DQEBBQUAMIGbMQswCQYDVQQGEwJKUDEOMAwG\n" +
            "A1UECBMFVG9reW8xEDAOBgNVBAcTB0NodW8ta3UxETAPBgNVBAoTCEZyYW5rNERE\n" +
            "MRgwFgYDVQQLEw9XZWJDZXJ0IFN1cHBvcnQxGDAWBgNVBAMTD0ZyYW5rNEREIFdl\n" +
            "YiBDQTEjMCEGCSqGSIb3DQEJARYUc3VwcG9ydEBmcmFuazRkZC5jb20wHhcNMTIw\n" +
            "ODIyMDUyNzIzWhcNMTcwODIxMDUyNzIzWjBKMQswCQYDVQQGEwJKUDEOMAwGA1UE\n" +
            "CAwFVG9reW8xETAPBgNVBAoMCEZyYW5rNEREMRgwFgYDVQQDDA93d3cuZXhhbXBs\n" +
            "ZS5jb20wgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAMYBBrx5PlP0WNI/ZdzD\n" +
            "+6Pktmurn+F2kQYbtc7XQh8/LTBvCo+P6iZoLEmUA9e7EXLRxgU1CVqeAi7QcAn9\n" +
            "MwBlc8ksFJHB0rtf9pmf8Oza9E0Bynlq/4/Kb1x+d+AyhL7oK9tQwB24uHOueHi1\n" +
            "C/iVv8CSWKiYe6hzN1txYe8rAgMBAAEwDQYJKoZIhvcNAQEFBQADgYEAASPdjigJ\n" +
            "kXCqKWpnZ/Oc75EUcMi6HztaW8abUMlYXPIgkV2F7YanHOB7K4f7OOLjiz8DTPFf\n" +
            "jC9UeuErhaA/zzWi8ewMTFZW/WshOrm3fNvcMrMLKtH534JKvcdMg6qIdjTFINIr\n" +
            "evnAhf0cwULaebn+lMs8Pdl7y37+sfluVok=\n" +
            "-----END CERTIFICATE-----";

    public static void main(String[] args) throws NoSuchAlgorithmException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, CertificateException {
        Encryptor encryptor =  new Encryptor();
        byte[] encryptedMessageBytes = encryptor.encryptMessage(message.getBytes(),encKeyString.getBytes());
        byte[] clearMessageBytes = encryptor.decryptMessage(encryptedMessageBytes, encKeyString.getBytes());
        System.out.println(message.equals(new String(clearMessageBytes)));

        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        InputStream is = new ByteArrayInputStream(certificateString.getBytes());
        X509Certificate certificate = (X509Certificate) factory.generateCertificate(is);
        byte[] encryptedMessage = encryptor.encryptMessage(message.getBytes(),certificate);
    }


    public byte[] encryptMessage(byte[] message, byte[] keyBytes) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey); // secretKey is more like a public key
        byte[] encryptedMessage = cipher.doFinal(message);
        return encryptedMessage;
    }

    public byte[] encryptMessage(byte[] message, Certificate publicKeyCertificate) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKeyCertificate);
        byte[] encryptedMessage = cipher.doFinal(message);
        return encryptedMessage;
    }

    public byte[] decryptMessage(byte[] encryptedMessage, byte[] keyBytes) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] clearMessage = cipher.doFinal(encryptedMessage);
        return clearMessage;
    }


}