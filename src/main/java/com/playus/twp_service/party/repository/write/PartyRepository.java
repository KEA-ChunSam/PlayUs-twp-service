package com.playus.twp_service.party.repository.write;

import com.playus.twp_service.party.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Long> {
}
