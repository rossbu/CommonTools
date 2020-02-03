package com.demo.datasecurity.encryption.certificate;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Base64;

import sun.security.tools.keytool.CertAndKeyGen;
import sun.security.x509.X500Name;
import sun.security.x509.X509Key;

public class SelfSignedCertificateGeneration {
    public static final int LINE_LENGTH = 64;
    public static final String BEGIN_CERT = "-----BEGIN CERTIFICATE-----";
    public static final String END_CERT = "-----END CERTIFICATE-----";
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static void main(String[] args){
        try{

            // in CertAndKeyGen.java , it would Generates a random public/private key pair internally. check the source.
            CertAndKeyGen keyGen=new CertAndKeyGen("RSA","SHA1WithRSA",null);

            keyGen.generate(1024);
            PrivateKey privateKey = keyGen.getPrivateKey();  // you can only get private key when generating the certificate, you won't be able to get private key after certificate signed/created.
            X509Key publicKey = keyGen.getPublicKey();

            //Generate self signed certificate
            X509Certificate[] chain=new X509Certificate[1];
            chain[0]=keyGen.getSelfCertificate(new X500Name("CN=ROOT"), (long)365*24*3600);
            byte[] encoded = chain[0].getEncoded();
            String encodedCert = Base64.getEncoder().encodeToString(encoded);
            System.out.println("Certificate : "+encodedCert);
            System.out.println();
            final Base64.Encoder encoder = Base64.getMimeEncoder(64, LINE_SEPARATOR.getBytes());
            final byte[] rawCrtText = chain[0].getEncoded();
            final String encodedCertText = new String(encoder.encode(rawCrtText));
            final String prettified_cert = BEGIN_CERT + LINE_SEPARATOR + encodedCertText + LINE_SEPARATOR + END_CERT;
            System.out.println(prettified_cert);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}