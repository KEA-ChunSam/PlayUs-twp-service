package com.playus.twpservice.domain.party.repository.read;

import com.playus.twpservice.domain.common.data.BaseMongoRepository;
import com.playus.twpservice.domain.party.document.PartyDocument;
import com.playus.twpservice.domain.party.repository.read.custom.PartyReadOnlyRepositoryCustom;
import org.springframework.data.mongodb.repository.Query;

public interface PartyReadOnlyRepository extends BaseMongoRepository<PartyDocument, Long>, PartyReadOnlyRepositoryCustom {

    @Query(value = "{'writer_id' : ?0}", exists = true)
    boolean existsByWriterId(Long writerId);
}
