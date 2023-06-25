package com.afra.urlshortener.model;

public class UrlErrorResponseDto {
    private String error;

    public UrlErrorResponseDto(String error) {
        this.error = error;
    }

    public UrlErrorResponseDto() {
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "UrlErrorResponseDto{" +
                "error='" + error + '\'' +
                '}';
    }
}
