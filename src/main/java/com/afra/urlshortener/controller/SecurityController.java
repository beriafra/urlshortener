package com.afra.urlshortener.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityController {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests( auth ->{
            auth
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/shortenUrl").authenticated()
                .requestMatchers("/urls").authenticated()
                .requestMatchers("/generateShortLink").authenticated()
                .requestMatchers("/generate").authenticated()
                .requestMatchers("/generate").authenticated()
                .requestMatchers("/").authenticated()
                .anyRequest().permitAll();
        })
        .csrf(AbstractHttpConfigurer::disable)
        .oauth2Login(Customizer.withDefaults())
        .formLogin(Customizer.withDefaults())
                .headers((headers) -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
        )
        .build();
    }
}
