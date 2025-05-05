package com.playus.twp_service.domain.chat.repository;

import com.playus.twp_service.domain.chat.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
}
