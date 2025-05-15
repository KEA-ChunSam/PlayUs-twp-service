package com.playus.twpservice.domain.party.repository.write;

import com.playus.twpservice.domain.party.entity.PartyThumbnailUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyThumbnailUrlRepository extends JpaRepository<PartyThumbnailUrl, Long> {
    void deleteByPartyId(Long partyId);
}
