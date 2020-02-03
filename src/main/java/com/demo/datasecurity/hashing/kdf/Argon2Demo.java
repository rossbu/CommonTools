package com.demo.datasecurity.hashing.kdf;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import java.time.Duration;
import java.time.Instant;


/**
    In 2013, a Password Hashing Competition (PHC) was held to develop a more resistant approach.
    On 20 July 2015 Argon2 was selected as the final PHC winner,
    with special recognition given to four other password hashing schemes: Catena, Lyra2, yescrypt and Makwa.[13]
 */
public class Argon2Demo {
    public static void main(String[] args) {
        Argon2Demo argon2Demo = new Argon2Demo();
        argon2Demo.run();

    }
    private void run() {
        String password = "Hello World!";
        Instant beginHash = Instant.now();

        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        System.out.println(String.format("Creating hash for password '%s'.", password));

        String hash = argon2.hash(4, 1024 * 1024, 8, password.toCharArray());
        System.out.println(String.format("Encoded hash is '%s'.", hash));

        Instant endHash = Instant.now();
        System.out.println(String.format(
                "Process took %f s",
                Duration.between(beginHash, endHash).toMillis() / 1024.0
        ));

        Instant beginVerify = Instant.now();
        System.out.println("Verifying hash...");

        boolean success = argon2.verify(hash, password.toCharArray());
        System.out.println(success ? "Success!" : "Failure!");

        Instant endVerify = Instant.now();
        System.out.println(String.format(
                "Process took %f s",
                Duration.between(beginVerify, endVerify).toMillis() / 1024.0
        ));
    }
}
