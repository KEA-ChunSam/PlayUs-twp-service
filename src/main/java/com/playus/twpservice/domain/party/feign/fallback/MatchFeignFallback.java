package com.playus.twpservice.domain.party.feign.fallback;

import com.playus.twpservice.domain.party.feign.client.MatchFeignClient;

import java.time.LocalDateTime;
import java.util.List;

public class MatchFeignFallback implements MatchFeignClient {

    @Override
    public List<LocalDateTime> getMatchDate(List<Long> matchIdList) {
        return List.of();
    }

}
