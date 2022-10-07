package com.cargodelivery.security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class PasswordEncoder {

    private static final String SALT = "salt";

    private static final int ITERATIONS = 1000;

    private static final int HASH_BYTE_SIZE = 256;

    private PasswordEncoder() {}

    public static String hash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), SALT.getBytes(), ITERATIONS, HASH_BYTE_SIZE);
        var hashedPassword = secretKeyFactory.generateSecret(keySpec).getEncoded();
        return Base64.getEncoder().encodeToString(hashedPassword);
    }
}
