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
import java.util.List;

@Controller
public class WebController {
    private final UrlService urlService;

    public WebController(UrlService urlService) {
        this.urlService = urlService;
    }


    @GetMapping("/")
    public String shortenUrl(Model model, Authentication authentication) {
        model.addAttribute("urlDto", new UrlDto());
        if(authentication != null){
            model.addAttribute("username", authentication.getName());
        }
        return "shortenUrl";
    }

    @PostMapping("/result")
    public String generateShortLink(@Valid @ModelAttribute UrlDto urlDto, BindingResult result, Model model, Authentication authentication) {
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", result.getFieldError().getDefaultMessage());
            return "result";
        }
        Url urlToRet = urlService.generateShortLinkWithUser(urlDto, authentication.getName());
        model.addAttribute("originalUrl", urlToRet.getOriginalUrl());
        model.addAttribute("shortUrl", urlToRet.getShortLink());
        return "result";
    }

    @GetMapping("/urls")
    public ModelAndView getAllUrlsForCurrentUser(Authentication authentication) {
        String userId = authentication.getName(); // Get the authenticated user's ID
        List<Url> urls = urlService.getAllUrlsForUser(userId);
        ModelAndView modelAndView = new ModelAndView("urls"); // The name of your Thymeleaf template
        modelAndView.addObject("urls", urls);
        modelAndView.addObject("username", userId);
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
        String userId = authentication.getName();
        urlService.enableUrl(shortLink, userId);
        return "redirect:/urls";
    }


}
