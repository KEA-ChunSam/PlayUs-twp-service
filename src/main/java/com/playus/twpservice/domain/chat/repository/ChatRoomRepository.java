package com.playus.twpservice.domain.chat.repository;

import com.playus.twpservice.domain.chat.entity.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {

}
