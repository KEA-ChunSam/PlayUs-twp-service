package com.playus.twpservice.domain.chat.repository;

import com.playus.twpservice.domain.chat.entity.ChatPart;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatPartRepository extends MongoRepository<ChatPart, String> {
}
