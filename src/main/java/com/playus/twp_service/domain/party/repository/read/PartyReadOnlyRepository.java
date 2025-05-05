package com.playus.twp_service.domain.party.repository.read;

import com.playus.twp_service.domain.party.document.PartyDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PartyReadOnlyRepository extends MongoRepository<PartyDocument, Long> {
}
