package com.playus.twpservice.domain.party.repository.write;

import com.playus.twpservice.domain.party.entity.PartyAge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyAgeRepository extends JpaRepository<PartyAge, Long> {
    void deleteByPartyId(Long partyId);
}
