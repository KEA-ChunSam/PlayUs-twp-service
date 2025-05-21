package com.playus.twpservice.domain.party.repository.read;

import com.playus.twpservice.domain.common.data.BaseMongoRepository;
import com.playus.twpservice.domain.party.document.PartyAgeDocument;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PartyAgeReadOnlyRepository extends BaseMongoRepository <PartyAgeDocument, Long> {
    @Query(value = "{'party_id' : ?0, 'deletedAt' : null}")
    List<PartyAgeDocument> findByPartyId(Long partyId);
}
