package com.playus.twpservice.domain.party.feign.fallback;

import com.playus.twpservice.domain.party.feign.client.UserFeignClient;
import com.playus.twpservice.domain.party.feign.response.PartyApplicantsInfoFeignResponse;
import com.playus.twpservice.domain.party.feign.response.PartyUserThumbnailUrlListResponse;
import com.playus.twpservice.domain.party.feign.response.PartyWriterInfoFeignResponse;
import lombok.extern.slf4j.Slf4j;
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
    public List<PartyApplicantsInfoFeignResponse> getPartyApplicantsInfo(List<Long> userIdList) {
        log.error("503 happened in UserFeignClient at fetching applicants data!!!");
        return List.of(PartyApplicantsInfoFeignResponse.withServiceUnavailable());
    }
}
