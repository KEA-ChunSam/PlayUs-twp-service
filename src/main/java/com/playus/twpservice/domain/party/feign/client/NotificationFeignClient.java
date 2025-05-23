package com.playus.twpservice.domain.party.feign.client;

import com.playus.twpservice.domain.party.feign.event.PartyNotificationEvent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notificationClient", url  = "${feign.user.url}", path = "/user/api")
public interface NotificationFeignClient {

    @PostMapping("/notifications/party")
    void notifyParty(@RequestBody PartyNotificationEvent event);

}
