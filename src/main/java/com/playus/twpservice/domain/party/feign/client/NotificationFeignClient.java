package com.playus.twpservice.domain.party.feign.client;

import com.playus.twpservice.domain.party.feign.event.PartyNotificationEvent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notificationClient", url  = "${feign.user.url}", path = "/user/notifications/parties")
public interface NotificationFeignClient {

    @PostMapping
    void notifyParty(@RequestBody PartyNotificationEvent event);

}
