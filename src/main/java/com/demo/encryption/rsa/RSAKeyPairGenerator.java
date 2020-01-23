package com.demo.encryption.rsa;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.util.Base64;


/**
 *  every time you run the program, you will get a different pair of private/public keys
 *
 *  the padding for OAEP or PKCS#1 v1.5 compatible padding is randomized
 */
public class RSAKeyPairGenerator {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public RSAKeyPairGenerator() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(512);
        KeyPair pair = keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public void writeToFile(String path, byte[] key) throws IOException {
        File f = new File(path);
        f.getParentFile().mkdirs();
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(key);
        fos.flush();
        fos.close();
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        RSAKeyPairGenerator keyPairGenerator = new RSAKeyPairGenerator();
//        keyPairGenerator.writeToFile("RSA/publicKey", keyPairGenerator.getPublicKey().getEncoded());
//        keyPairGenerator.writeToFile("RSA/privateKey", keyPairGenerator.getPrivateKey().getEncoded());
        System.out.println("public key: \n" +Base64.getEncoder().encodeToString(keyPairGenerator.getPublicKey().getEncoded()));
        System.out.println("private key: \n" + Base64.getEncoder().encodeToString(keyPairGenerator.getPrivateKey().getEncoded()));
    }
}