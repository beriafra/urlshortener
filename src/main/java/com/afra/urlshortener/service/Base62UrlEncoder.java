package com.afra.urlshortener.service;

public class Base62UrlEncoder {
    private static final String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String encode(long value) {
        StringBuilder encodedValue = new StringBuilder();
        while (value != 0) {
            encodedValue.append(BASE62_CHARS.charAt((int) (value % 62)));
            value /= 62;
        }
        return encodedValue.reverse().toString();
    }
}
