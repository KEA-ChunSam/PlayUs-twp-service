package com.playus.twpservice.domain.party.feign.fallback;

import com.playus.twpservice.domain.party.feign.client.UserFeignClient;
import com.playus.twpservice.domain.party.feign.response.PartyWriterInfoFeignResponse;

import java.util.List;

public class UserFeignFallback implements UserFeignClient {

    @Override
    public List<PartyWriterInfoFeignResponse> getWriterInfo(List<Long> writerIdList) {
        return List.of();
    }

    @Override
    public List<String> getPartyUserThumbnailUrls(List<Long> userIdList) {
        return List.of();
    }
}
