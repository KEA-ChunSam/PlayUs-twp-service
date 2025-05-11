package com.playus.twpservice.domain.party.repository.read;

import com.playus.twpservice.domain.party.document.PartyAgeDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PartyAgeReadOnlyRepository extends MongoRepository<PartyAgeDocument, Long> {
}
