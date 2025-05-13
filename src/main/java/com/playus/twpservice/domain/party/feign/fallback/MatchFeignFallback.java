package com.playus.twpservice.domain.party.feign.fallback;

import com.playus.twpservice.domain.party.feign.client.MatchFeignClient;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MatchFeignFallback implements MatchFeignClient {

    @Override
    public LocalDateTime getMatchDate(Long matchId) {
        return LocalDateTime.of(2023, 10, 1, 0, 0);
    }
}
