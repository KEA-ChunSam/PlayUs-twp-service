package com.playus.twpservice.domain.chat.repository.read;

import com.playus.twpservice.domain.chat.document.ChatParticipantDocument;
import com.playus.twpservice.domain.common.data.BaseMongoRepository;

import java.util.List;

public interface ChatParticipantReadOnlyRepository extends BaseMongoRepository<ChatParticipantDocument, Long> {

    List<ChatParticipantDocument> findByChatRoomId(long chatRoomId);

    long countByChatRoomId(long chatRoomId);
}
