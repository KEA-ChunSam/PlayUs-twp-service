package com.playus.twpservice.domain.party.feign.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;

// match-service, /match/api 예상 중
@FeignClient(name = "${feign.match.domain}", url = "${feign.match.url}", fallback = MatchFeignClient.class)
@CircuitBreaker(name = "circuit")
public interface MatchFeignClient {

    List<LocalDateTime> getMatchDate(@RequestBody List<Long> matchIdList);
}
