package com.playus.twpservice.domain.party.repository.read;

import com.playus.twpservice.domain.common.BaseMongoRepository;
import com.playus.twpservice.domain.party.document.PartyJoinDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PartyJoinReadOnlyRepository extends BaseMongoRepository<PartyJoinDocument, Long> {
}
