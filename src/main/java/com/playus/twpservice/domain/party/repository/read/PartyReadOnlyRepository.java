package com.playus.twpservice.domain.party.repository.read;

import com.playus.twpservice.domain.common.data.BaseMongoRepository;
import com.playus.twpservice.domain.party.document.PartyDocument;
import com.playus.twpservice.domain.party.repository.read.custom.PartyReadOnlyRepositoryCustom;

public interface PartyReadOnlyRepository extends BaseMongoRepository<PartyDocument, Long>, PartyReadOnlyRepositoryCustom {

}
