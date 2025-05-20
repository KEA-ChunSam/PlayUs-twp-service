package com.playus.twpservice.domain.party.repository.read;

import com.playus.twpservice.domain.common.data.BaseMongoRepository;
import com.playus.twpservice.domain.party.document.PartyAgeDocument;

public interface PartyAgeReadOnlyRepository extends BaseMongoRepository <PartyAgeDocument, Long> {
}
