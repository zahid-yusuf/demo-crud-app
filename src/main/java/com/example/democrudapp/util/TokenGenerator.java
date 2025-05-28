package com.example.democrudapp.util;

import org.antlr.v4.runtime.Token;

import java.security.SecureRandom;

public final class TokenGenerator {
    private static final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom secureRandom = new SecureRandom();

    private TokenGenerator() {

    }

    public static String generateToken(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Token length must be positive");
        }

        StringBuilder token = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = secureRandom.nextInt(CHARACTERS.length());
            token.append(CHARACTERS.charAt(index));
        }
        return token.toString();
    }

}
