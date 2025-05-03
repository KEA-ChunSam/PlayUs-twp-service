package com.playus.twp_service.global.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RequiredArgsConstructor
public class JwtFilter implements WebFilter {

    private final JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authorization = null;

        ServerHttpRequest request = exchange.getRequest();

        if (request.getCookies().containsKey("Authorization")) {
            authorization = Objects.requireNonNull(request.getCookies().getFirst("Authorization")).getValue();
        }

        if (authorization == null || jwtUtil.isExpired(authorization)) {
            return chain.filter(exchange);
        }

        Long userId = Long.parseLong(jwtUtil.getUserId(authorization));
        Authentication authToken = new UsernamePasswordAuthenticationToken(userId, null);
        SecurityContextHolder.getContext().setAuthentication(authToken);

        return chain.filter(exchange);
    }
}
