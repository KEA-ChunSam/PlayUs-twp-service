package com.playus.twpservice.domain.party.repository.write;

import com.playus.twpservice.domain.party.entity.PartyThumbnailUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PartyThumbnailUrlRepository extends JpaRepository<PartyThumbnailUrl, Long> {
    void deleteByPartyId(Long partyId);

    @Query(value = "SELECT * FROM party_thumbnail_url WHERE deleted_at IS NOT NULL AND deleted_at < :threshold", nativeQuery = true)
    List<PartyThumbnailUrl> findAllDeletedPartyThumbnailUrlBefore(@Param("threshold") LocalDateTime threshold);

}
