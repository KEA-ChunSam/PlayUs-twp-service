package com.playus.twpservice.domain.party.repository.read;

import com.playus.twpservice.domain.party.document.PartyJoinDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PartyJoinReadOnlyRepository extends MongoRepository<PartyJoinDocument, Long> {
}
