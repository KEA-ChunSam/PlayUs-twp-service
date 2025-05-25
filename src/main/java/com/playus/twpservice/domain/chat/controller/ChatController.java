package com.playus.twpservice.domain.chat.controller;

import com.playus.twpservice.domain.chat.dto.request.ChatMessageRequest;
import com.playus.twpservice.domain.chat.dto.response.ChatResponse;
import com.playus.twpservice.domain.chat.dto.response.ChatUserInfoResponse;
import com.playus.twpservice.domain.chat.service.ChatRoomService;
import com.playus.twpservice.domain.chat.service.ChattingService;
import com.playus.twpservice.domain.chat.specification.ChatControllerSpecification;
import com.playus.twpservice.domain.common.security.CustomOAuth2User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ChatController implements ChatControllerSpecification {

    private final ChattingService chattingService;
    private final ChatRoomService chattingRoomService;

    @Override
    @GetMapping("/chat/{roomId}")
    public ResponseEntity<ChatResponse> getChatMessages(@AuthenticationPrincipal CustomOAuth2User principal,
                                                        @PathVariable Long roomId,
                                                        @RequestParam @Min(0) int pageNumber,
                                                        @RequestParam @Min(0) @Max(100) int pageSize,
                                                        @RequestParam(required = false) LocalDateTime lastMessageTimeStamp) {
        ChatResponse response = chattingService.getMessage(roomId, principal.getId(), pageNumber, pageSize, lastMessageTimeStamp);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @GetMapping("/chat/count/{roomId}")
    public ResponseEntity<ChatUserInfoResponse> getChatParticipants(@AuthenticationPrincipal CustomOAuth2User principal,
                                                                    @PathVariable Long roomId) {
        ChatUserInfoResponse response = chattingRoomService.getCount(principal.getId(), roomId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @DeleteMapping("/chat/{roomId}")
    public ResponseEntity<Void> exitChatRoom(@AuthenticationPrincipal CustomOAuth2User principal,
                                             @PathVariable Long roomId) {
        chattingRoomService.exitChatRoom(roomId, principal.getId());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    @MessageMapping("/chat/message")
    public void message(@Valid ChatMessageRequest request, SimpMessageHeaderAccessor headerAccessor) {
        chattingService.chat(request, headerAccessor);
    }
}
