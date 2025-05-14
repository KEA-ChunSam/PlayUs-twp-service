package com.playus.twpservice.domain.party.repository.read;

import com.playus.twpservice.domain.party.document.PartyDocument;
import com.playus.twpservice.domain.party.repository.read.custom.PartyReadOnlyRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PartyReadOnlyRepository extends MongoRepository<PartyDocument, Long>, PartyReadOnlyRepositoryCustom {

}
