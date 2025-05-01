package com.playus.twp_service;

import com.playus.twp_service.party.controller.PartyController;
import com.playus.twp_service.party.service.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

@ActiveProfiles("test")
@WebFluxTest(controllers = {
        PartyController.class
})
public abstract class ControllerTestSupport {

    @Autowired
    protected WebTestClient webTestClient;

    @MockitoBean
    protected PartyService partyService;
}
