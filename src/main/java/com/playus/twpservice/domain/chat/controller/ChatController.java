package com.playus.twpservice.domain.chat.controller;

import com.playus.twpservice.domain.chat.dto.request.ChatMessageRequest;
import com.playus.twpservice.domain.chat.dto.response.ChatResponse;
import com.playus.twpservice.domain.chat.dto.response.ChatUserInfoResponse;
import com.playus.twpservice.domain.chat.service.ChatRoomService;
import com.playus.twpservice.domain.chat.service.ChattingService;
import com.playus.twpservice.domain.common.security.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
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
public class ChatController {

    private final ChattingService chattingService;
    private final ChatRoomService chattingRoomService;

    @GetMapping("/chat/{roomId}")
    @Operation(summary = "채팅방 재입장시 이전 메시지를 조회하기 위한 API입니다.")
    public ResponseEntity<ChatResponse> getChattingMessages(@PathVariable Long roomId,
                                                            @AuthenticationPrincipal CustomOAuth2User principal,
                                                            @RequestParam int pageNumber,
                                                            @RequestParam int pageSize,
                                                            @RequestParam(required = false) LocalDateTime lastMessageTimeStamp) {
        ChatResponse response = chattingService.getMessage(roomId, principal.getId(), pageNumber, pageSize, lastMessageTimeStamp);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/chat/count/{roomId}")
    @Operation(summary = "채팅방의 총 참여자 수와 참여 멤버를 조회하기 위한 API입니다.")
    public ResponseEntity<ChatUserInfoResponse> getChattingMessageCount(@AuthenticationPrincipal CustomOAuth2User principal,
                                                                     @PathVariable Long roomId) {
        ChatUserInfoResponse response = chattingRoomService.getCount(principal.getId(), roomId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/chat/{roomId}")
    @Operation(summary = "채팅방을 퇴장하는 API입니다. 퇴장시 STOMP UNSUBSCRIBE를 꼭 해주세요")
    public ResponseEntity<Void> exitChattingRoom(@PathVariable Long roomId,
                                              @AuthenticationPrincipal CustomOAuth2User principal) {
        chattingRoomService.exitChatRoom(roomId, principal.getId());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @MessageMapping("/chat/message")
    public void message(@Valid ChatMessageRequest request, SimpMessageHeaderAccessor headerAccessor) {
        chattingService.chat(request, headerAccessor);
    }
}
