package com.afra.urlshortener.controller;

import com.afra.urlshortener.model.Url;
import com.afra.urlshortener.model.UrlDto;
import com.afra.urlshortener.model.UrlErrorResponseDto;
import com.afra.urlshortener.model.UrlResponseDto;
import com.afra.urlshortener.service.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class UrlShortenerController {
    private final UrlService urlService;

    public UrlShortenerController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/{shortLink}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortLink, HttpServletResponse response) throws IOException {
        if(StringUtils.isEmpty(shortLink)) {
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto("Invalid Url");
            return new ResponseEntity<>(urlErrorResponseDto, HttpStatus.BAD_REQUEST);

        }

        Url encodedUrl = urlService.getEncodedUrl(shortLink);

        if(encodedUrl == null){
            return new ResponseEntity<>("The provided short URL does not exist!", HttpStatus.NOT_FOUND);
        }

        if(!encodedUrl.getEnabled()){
            return new ResponseEntity<>("This url has been disabled by owner!", HttpStatus.FORBIDDEN);
        }

        if(encodedUrl.getExpirationDate().isBefore(LocalDateTime.now())) {
            urlService.deleteShortLink(encodedUrl);
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto("Url Expired. Please try generating a fresh one.");
            return new ResponseEntity<>(urlErrorResponseDto, HttpStatus.GONE);
        }

        encodedUrl.setClickCount(encodedUrl.getClickCount() + 1);
        encodedUrl.setLastAccessed(LocalDateTime.now());
        urlService.persistShortLink(encodedUrl);

        response.sendRedirect(encodedUrl.getOriginalUrl());
        return ResponseEntity.status(HttpStatus.FOUND).build(); // 302 - Found, used for redirection
    }

    @GetMapping("/users/urls")
    public ResponseEntity<List<Url>> getAllUrlsForUser(Authentication authentication) {
        List<Url> urls = urlService.getAllUrlsForUser(authentication.getName());
        return ResponseEntity.ok(urls); // 200 - OK
    }

    @PutMapping("/enable/{shortLink}")
    public ResponseEntity<String> enableUrl(@PathVariable String shortLink, Authentication authentication) {
        try {
            urlService.enableUrl(shortLink, authentication.getName());
            return ResponseEntity.noContent().build(); // 204 - No Content
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()); // 500 - Internal Server Error
        }
    }

    @PutMapping("/disable/{shortLink}")
    public ResponseEntity<String> disableUrl(@PathVariable String shortLink, Authentication authentication) {
        try {
            urlService.disableUrl(shortLink, authentication.getName());
            return ResponseEntity.noContent().build(); // 204 - No Content
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()); // 500 - Internal Server Error
        }
    }
}
