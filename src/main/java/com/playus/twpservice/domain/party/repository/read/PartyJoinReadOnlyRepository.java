package com.playus.twpservice.domain.party.repository.read;

import com.playus.twpservice.domain.common.data.BaseMongoRepository;
import com.playus.twpservice.domain.party.document.PartyJoinDocument;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface PartyJoinReadOnlyRepository extends BaseMongoRepository<PartyJoinDocument, Long> {

    @Query(value = "{'userId': ?0, 'partyId': ?1, 'deletedAt' : null}")
    Optional<PartyJoinDocument> findByUserIdAndPartyId(Long userId, Long partyId);
}
