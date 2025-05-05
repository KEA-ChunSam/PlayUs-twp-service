package com.playus.twp_service.domain.party.repository.write;

import com.playus.twp_service.domain.party.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Long> {
}
