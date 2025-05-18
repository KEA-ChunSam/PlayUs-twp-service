package com.playus.twpservice.domain.chat.repository;

import com.playus.twpservice.domain.chat.entity.ChatPart;
import com.playus.twpservice.domain.common.data.BaseMongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.util.List;

public interface ChatPartRepository extends BaseMongoRepository<ChatPart, String> {


    @Query("{ 'chatroom_id': ?0, is_deleted: false }")
    @Update("{ '$set' : { 'deleted_at' : new Date(), 'is_deleted': true } }")
    void deleteByChatRoomId(String chatRoomId);

    @Query("{ chatroom_id: ?0, is_deleted :  false}")
    List<ChatPart> findByChatRoomId(String chatRoomId);
}
