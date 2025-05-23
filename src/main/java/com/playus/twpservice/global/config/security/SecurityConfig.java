package com.playus.twpservice.global.config.security;

import com.playus.twpservice.global.jwt.JwtFilter;
import com.playus.twpservice.global.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final CorsConfigurationSource corsConfigurationSource;

    private String [] getWhiteList() {
        return new String[] {
                "/error",
                "/swagger",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/webjars/**",
                "/api-docs",
                "/api-docs/**",
                "/v3/api-docs/**",
                "/oauth2/authorization/kakao",
                "/login/oauth2/code/kakao",
                "/oauth2/authorization/naver",
                "/login/oauth2/code/naver",
                "/api/v1/auth/reissue",
                "/api/v1/auth/logout",
                "/ws/**",
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 등록
                .addFilterBefore(
                        new JwtFilter(jwtUtil, redisTemplate),
                        UsernamePasswordAuthenticationFilter.class
                )

                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                // 인증/인가
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(getWhiteList())
                        .permitAll()
                        .anyRequest().authenticated()
                )

                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
