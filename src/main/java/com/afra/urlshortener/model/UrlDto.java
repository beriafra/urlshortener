package com.afra.urlshortener.model;

import org.hibernate.validator.constraints.URL;


public class UrlDto {
    @URL(message = "Please enter a valid URL")
    private String url;
    private String expirationDate;

    public UrlDto(String url, String expirationDate) {
        this.url = url;
        this.expirationDate = expirationDate;
    }

    public UrlDto(String url) {
        this.url = url;
    }

    public UrlDto() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

}
