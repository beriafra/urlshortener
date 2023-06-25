package com.afra.urlshortener.service;

public class Base62UrlEncoder {
    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String encode(int value) {
        StringBuilder sb = new StringBuilder();
        while (value != 0) {
            sb.append(ALPHABET.charAt(value % 62));
            value /= 62;
        }
        return sb.reverse().toString();
    }
    public static int decode(String value) {
        int result = 0;
        for (char c : value.toCharArray()) {
            result = result * 62 + ALPHABET.indexOf(c);
        }
        return result;
    }
}
