package com.afra.urlshortener.controller;

import com.afra.urlshortener.model.Url;
import com.afra.urlshortener.model.UrlDto;
import com.afra.urlshortener.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
public class WebController {
    private final UrlService urlService;

    public WebController(UrlService urlService) {
        this.urlService = urlService;
    }


    @GetMapping("/")
    public String shortenUrl(Model model) {
        model.addAttribute("urlDto", new UrlDto());
        return "shortenUrl";
    }

    @PostMapping("/generateShortLink")
    public String generateShortLink(@Valid @ModelAttribute UrlDto urlDto, BindingResult result, Model model, Authentication authentication) {
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            return "result";
        }
        Url urlToRet = urlService.generateShortLinkWithUser(urlDto, authentication.getName());
        model.addAttribute("originalUrl", urlToRet.getOriginalUrl());
        model.addAttribute("shortUrl", urlToRet.getShortLink());
        return "result";
    }

    @GetMapping("/urls")
    public ModelAndView getAllUrlsForCurrentUser(Authentication authentication) {
        String userId = authentication.getName();
        List<Url> urls = urlService.getAllUrlsForUser(userId);
        ModelAndView modelAndView = new ModelAndView("urls");
        modelAndView.addObject("urls", urls);
        return modelAndView;
    }

    @PostMapping("/disable/{shortLink}")
    public String disableUrl(@PathVariable String shortLink, Authentication authentication) {
        String userId = authentication.getName(); // Get the authenticated user's ID
        urlService.disableUrl(shortLink, userId);
        return "redirect:/urls";
    }

    @PostMapping("/enable/{shortLink}")
    public String enableUrl(@PathVariable String shortLink, Authentication authentication) {
        String userId = authentication.getName(); // Get the authenticated user's ID
        urlService.enableUrl(shortLink, userId);
        return "redirect:/urls";
    }


}
