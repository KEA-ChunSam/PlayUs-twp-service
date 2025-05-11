package com.playus.twpservice.domain.party.repository.read;

import com.playus.twpservice.domain.party.document.PartyThumbnailUrlDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PartyThumbnailUrlReadOnlyRepository extends MongoRepository<PartyThumbnailUrlDocument, Long> {
}
