package com.playus.twpservice.global.config.security;

import com.playus.twpservice.global.jwt.JwtFilter;
import com.playus.twpservice.global.jwt.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;


import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final CorsConfigurationSource corsConfigurationSource;

    private static final List<String> ALLOWED_ORIGINS = List.of(
            "http://localhost:3000",
            "http://localhost:8080"
    );

    private String [] getWhiteList() {
        return new String[] {
                "/swagger",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/api-docs",
                "/api-docs/**",
                "/v3/api-docs/**",
                "/oauth2/authorization/kakao",
                "/login/oauth2/code/kakao",
                "/oauth2/authorization/naver",
                "/login/oauth2/code/naver",
                "/api/v1/auth/reissue",
                "/api/v1/auth/logout",
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                // JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 등록
                .addFilterBefore(
                        new JwtFilter(jwtUtil, redisTemplate),
                        UsernamePasswordAuthenticationFilter.class
                )

                .cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest req) {
                        CorsConfiguration c = new CorsConfiguration();
                        c.setAllowedOrigins(ALLOWED_ORIGINS);
                        c.setAllowedMethods(Collections.singletonList("*"));
                        c.setAllowedHeaders(Collections.singletonList("*"));
                        c.setAllowCredentials(true);
                        c.setExposedHeaders(Collections.singletonList("Authorization"));
                        return c;
                    }
                }))

                // JWT Resource Server 설정 추가
                .oauth2ResourceServer(rs -> rs
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder())
                        )
                )

                // 인증/인가
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(getWhiteList())
                        .permitAll()
                        .anyRequest().authenticated()
                )

                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return jwtUtil.jwtDecoder();
    }
}
