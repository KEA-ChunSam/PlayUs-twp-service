package com.playus.twp_service.party.repository.read;

import com.playus.twp_service.party.document.PartyDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PartyReadOnlyRepository extends MongoRepository<PartyDocument, Long> {
}
