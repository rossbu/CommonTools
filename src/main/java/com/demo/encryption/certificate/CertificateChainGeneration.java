package com.demo.encryption.certificate;

import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import java.util.Arrays;

import javax.security.cert.Certificate;


import sun.security.x509.BasicConstraintsExtension;
import sun.security.tools.keytool.CertAndKeyGen;
import sun.security.x509.CertificateExtensions;
import sun.security.x509.CertificateIssuerName;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;

//Tested in jdk1.8.0_40
public class CertificateChainGeneration {
    public static void main(String[] args){
        try{
            //Generate ROOT certificate
            CertAndKeyGen keyGen=new CertAndKeyGen("RSA","SHA1WithRSA",null);
            keyGen.generate(1024);
            PrivateKey rootPrivateKey=keyGen.getPrivateKey();

            X509Certificate rootCertificate = keyGen.getSelfCertificate(new X500Name("CN=ROOT"), (long) 365 * 24 * 60 * 60);

            //Generate intermediate certificate
            CertAndKeyGen keyGen1=new CertAndKeyGen("RSA","SHA1WithRSA",null);
            keyGen1.generate(1024);
            PrivateKey middlePrivateKey=keyGen1.getPrivateKey();

            X509Certificate middleCertificate = keyGen1.getSelfCertificate(new X500Name("CN=MIDDLE"), (long) 365 * 24 * 60 * 60);

            //Generate leaf certificate
            CertAndKeyGen keyGen2=new CertAndKeyGen("RSA","SHA1WithRSA",null);
            keyGen2.generate(1024);
            PrivateKey topPrivateKey=keyGen2.getPrivateKey();

            X509Certificate topCertificate = keyGen2.getSelfCertificate(new X500Name("CN=TOP"), (long) 365 * 24 * 60 * 60);

            rootCertificate   = createSignedCertificate(rootCertificate,rootCertificate,rootPrivateKey);
            middleCertificate = createSignedCertificate(middleCertificate,rootCertificate,rootPrivateKey);
            topCertificate    = createSignedCertificate(topCertificate,middleCertificate,middlePrivateKey);

            X509Certificate[] chain = new X509Certificate[3];
            chain[0]=topCertificate;
            chain[1]=middleCertificate;
            chain[2]=rootCertificate;

            System.out.println(Arrays.toString(chain));
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private static X509Certificate createSignedCertificate(X509Certificate cetrificate,X509Certificate issuerCertificate,PrivateKey issuerPrivateKey){
        try{
            Principal issuer = issuerCertificate.getSubjectDN();
            String issuerSigAlg = issuerCertificate.getSigAlgName();

            byte[] inCertBytes = cetrificate.getTBSCertificate();
            X509CertInfo info = new X509CertInfo(inCertBytes);
            info.set(X509CertInfo.ISSUER, (X500Name) issuer);

            //No need to add the BasicContraint for leaf cert
            if(!cetrificate.getSubjectDN().getName().equals("CN=TOP")){
                CertificateExtensions exts=new CertificateExtensions();
                BasicConstraintsExtension bce = new BasicConstraintsExtension(true, -1);
                exts.set(BasicConstraintsExtension.NAME,new BasicConstraintsExtension(false, bce.getExtensionValue()));
                info.set(X509CertInfo.EXTENSIONS, exts);
            }

            X509CertImpl outCert = new X509CertImpl(info);
            outCert.sign(issuerPrivateKey, issuerSigAlg);

            return outCert;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}