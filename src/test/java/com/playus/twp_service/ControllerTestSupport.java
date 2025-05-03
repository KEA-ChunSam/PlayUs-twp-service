package com.playus.twp_service;

import com.playus.twp_service.party.controller.PartyController;
import com.playus.twp_service.party.service.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;


@ActiveProfiles("test")
@WebFluxTest(controllers = {
        PartyController.class
})
@Import({ControllerTestSupport.TestSecurityConfig.class})
public abstract class ControllerTestSupport {

    @Autowired
    protected WebTestClient webTestClient;

    @MockitoBean
    protected PartyService partyService;

    @TestConfiguration
    @EnableWebFluxSecurity
    static class TestSecurityConfig {

        @Bean
        public SecurityWebFilterChain testSecurityFilterChain(ServerHttpSecurity http) {
            return http
                    .csrf(ServerHttpSecurity.CsrfSpec::disable)
                    .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                    .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                    .authorizeExchange(exchanges -> exchanges
                            .anyExchange().hasAuthority("USER")
                    )
                    .build();
        }
    }
}
