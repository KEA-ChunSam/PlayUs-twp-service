package com.playus.twpservice.domain.party.repository.write;

import com.playus.twpservice.domain.party.entity.PartyJoin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyJoinRepository extends JpaRepository<PartyJoin, Long> {
    void deleteByPartyId(Long partyId);
}
