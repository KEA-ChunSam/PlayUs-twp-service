package com.playus.twpservice.domain.party.feign.fallback;

import com.playus.twpservice.domain.party.feign.client.UserFeignClient;
import com.playus.twpservice.domain.party.feign.response.PartyUserThumbnailUrlListResponse;
import com.playus.twpservice.domain.party.feign.response.PartyWriterInfoFeignResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserFeignFallback implements UserFeignClient {

    @Override
    public List<PartyWriterInfoFeignResponse> getWriterInfo(List<Long> writerIdList) {
        return List.of(PartyWriterInfoFeignResponse.withServiceUnavailable());
    }

    @Override
    public PartyUserThumbnailUrlListResponse getPartyUserThumbnailUrls(List<Long> userIdList) {
        return PartyUserThumbnailUrlListResponse.withServiceUnavailable();
    }
}
