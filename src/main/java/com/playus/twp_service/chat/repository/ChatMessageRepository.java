package com.playus.twp_service.chat.repository;

import com.playus.twp_service.chat.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
}
