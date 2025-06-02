package com.playus.twpservice.global.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
public class CorsConfig {

    private static final List<String> ALLOWED_ORIGINS = List.of(
            "*"
    );

    @Bean("corsConfigurationSource")
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(ALLOWED_ORIGINS);
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(false);
        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

        configuration.addAllowedMethod(HttpMethod.GET.name());
        configuration.addAllowedMethod(HttpMethod.POST.name());
        configuration.addAllowedMethod(HttpMethod.PUT.name());
        configuration.addAllowedMethod(HttpMethod.DELETE.name());
        configuration.addAllowedMethod(HttpMethod.OPTIONS.name());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
