package com.demo.datasecurity.hashing.kdf;

import com.lambdaworks.crypto.SCryptUtil;


/**
 * ref: https://github.com/wg/scrypt
 */
public class ScryptDemo {

    public static void main(String[] args) {
        String password = "admin123";
        String hashedPassword = SCryptUtil.scrypt(password, 16, 16, 16);
        System.out.println(hashedPassword);

        boolean matched = SCryptUtil.check("admin123", hashedPassword);
        System.out.println(matched);

        matched = SCryptUtil.check("wrongone", hashedPassword);
        System.out.println(matched);
    }
}
