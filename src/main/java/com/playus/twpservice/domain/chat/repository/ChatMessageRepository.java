package com.playus.twpservice.domain.chat.repository;

import com.playus.twpservice.domain.chat.entity.ChatMessage;
import com.playus.twpservice.domain.common.BaseMongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.util.List;

public interface ChatMessageRepository extends BaseMongoRepository<ChatMessage, String> {

    @Query("{ 'chatpart_id' : {$in :  ?0}, is_deleted : false}")
    @Update("{ '$set' : { 'deleted_at' : new Date(), 'is_deleted': true } }")
    void deleteAllByChatPartIds(List<String> chatPartIds);
}
