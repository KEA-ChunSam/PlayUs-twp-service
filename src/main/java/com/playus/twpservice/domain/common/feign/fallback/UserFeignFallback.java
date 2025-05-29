package com.playus.twpservice.domain.common.feign.fallback;

import com.playus.twpservice.domain.common.feign.client.UserFeignClient;
import com.playus.twpservice.domain.common.feign.request.TokenValidationRequest;
import com.playus.twpservice.domain.common.feign.response.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class UserFeignFallback implements UserFeignClient {

    @Override
    public List<PartyWriterInfoFeignResponse> getWriterInfo(List<Long> writerIdList) {
        log.error("503 happened in UserFeignClient at fetching writer data!!!");
        return List.of(PartyWriterInfoFeignResponse.withServiceUnavailable());
    }

    @Override
    public PartyUserThumbnailUrlListResponse getPartyUserThumbnailUrls(List<Long> userIdList) {
        log.error("503 happened in UserFeignClient at fetching user thumbnail data!!!");
        return PartyUserThumbnailUrlListResponse.withServiceUnavailable();
    }

    @Override
    public UserInfoResponse getUserInfo(Long userId) {
        log.error("503 happened in UserFeignClient at fetching user data!!!");
        return UserInfoResponse.withServiceUnavailable();
    }

    @Override
    public List<PartyParticipantsInfoFeignResponse> getPartyApplicantsInfo(List<Long> userIdList) {
        log.error("503 happened in UserFeignClient at fetching applicants data!!!");
        return List.of(PartyParticipantsInfoFeignResponse.withServiceUnavailable());
    }

    @Override
    public ResponseEntity<TokenValidationResponse> checkBlackList(TokenValidationRequest req) {
        log.error("503 happened in UserFeignClient at validating token!!!");
        return ResponseEntity.internalServerError().body(TokenValidationResponse.of(true));
    }


}
