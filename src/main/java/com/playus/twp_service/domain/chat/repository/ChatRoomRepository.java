package com.playus.twp_service.domain.chat.repository;

import com.playus.twp_service.domain.chat.entity.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {

}
