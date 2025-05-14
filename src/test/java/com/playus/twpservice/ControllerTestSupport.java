package com.playus.twpservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playus.twpservice.domain.party.controller.PartyController;
import com.playus.twpservice.domain.party.service.PartyReadOnlyService;
import com.playus.twpservice.domain.party.service.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


@ActiveProfiles("test")
@WebMvcTest(controllers = {
        PartyController.class
})
@Import({ControllerTestSupport.TestSecurityConfig.class})
public abstract class ControllerTestSupport {


    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockitoBean
    protected PartyService partyService;

    @MockitoBean
    protected PartyReadOnlyService partyReadOnlyService;

    @TestConfiguration
    static class TestSecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable);
            http.formLogin(AbstractHttpConfigurer::disable);
            http.httpBasic(AbstractHttpConfigurer::disable);
            http.authorizeHttpRequests(auth -> auth
                    .anyRequest().hasAuthority("USER")
            );

            // 세션 관리: Stateless
            http.sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

            return http.build();

        }
    }
}
