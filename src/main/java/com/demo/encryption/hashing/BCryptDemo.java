package com.demo.encryption.hashing;

import org.mindrot.jbcrypt.BCrypt;

import java.util.function.Function;

/**
 * Storing passwords in plain text in databse is vulnerable to security.
 * This article is about storing hashed password to databse in java.
 * Doing so it becomes impossible for even BDAs to extract the real passwords.
 *
 * There are many hashing algorithms such as MD5, SHA-1, SHA-2 etc to hash a password but adding a salt to the password provides extra security.
 *
 *
 * jBCrypt internally generates a random salt while encoding passwords, to encode our plain text passwords and store in DB.
 */
public class BCryptDemo {

    public static void main(String[] args) {
        String pw1 = "\u2605\u2605\u2605\u2605\u2605\u2605\u2605\u2605";
        String pw2 = "????????";
        String pw3 = "admin123";

        String h1 = BCrypt.hashpw(pw1, BCrypt.gensalt());
        System.out.println("h1 : " + h1); System.out.println(BCrypt.checkpw(pw1, h1));

        String h2 = BCrypt.hashpw(pw2, BCrypt.gensalt());
        System.out.println("h2 : " + h2); System.out.println(BCrypt.checkpw(pw2, h2));

        final String hashedPassword = BCrypt.hashpw(pw3, BCrypt.gensalt(14));
        System.out.println("hashedPassword : " + hashedPassword);


    }

    private final int logRounds;

    public BCryptDemo(int logRounds) {
        this.logRounds = logRounds;
    }

    public String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(logRounds));
    }
    private void checkPass(String plainPassword, String hashedPassword) {
        if (BCrypt.checkpw(plainPassword, hashedPassword)) {
            System.out.println("The password matches.");
        } else {
            System.out.println("The password does not match.");
        }
    }
    public boolean verifyHash(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }

    public boolean verifyAndUpdateHash(String password, String hash, Function<String, Boolean> updateFunc) {
        if (BCrypt.checkpw(password, hash)) {
            int rounds = getRounds(hash);
            // It might be smart to only allow increasing the rounds.
            // If someone makes a mistake the ability to undo it would be nice though.
            if (rounds != logRounds) {
                System.out.println("Updating password from {} rounds to {}" + rounds + logRounds);
                String newHash = hash(password);
                return updateFunc.apply(newHash);
            }
            return true;
        }
        return false;
    }

    /*
     * Copy pasted from BCrypt internals :(. Ideally a method
     * to exports parts would be public. We only care about rounds
     * currently.
     */
    private int getRounds(String salt) {
        char minor = (char) 0;
        int off = 0;

        if (salt.charAt(0) != '$' || salt.charAt(1) != '2') {
            throw new IllegalArgumentException("Invalid salt version");
        }
        if (salt.charAt(2) == '$') {
            off = 3;
        } else {
            minor = salt.charAt(2);
            if (minor != 'a' || salt.charAt(3) != '$') {
                throw new IllegalArgumentException("Invalid salt revision");
            }
            off = 4;
        }

        // Extract number of rounds
        if (salt.charAt(off + 2) > '$') {
            throw new IllegalArgumentException("Missing salt rounds");
        }
        return Integer.parseInt(salt.substring(off, off + 2));
    }
}
