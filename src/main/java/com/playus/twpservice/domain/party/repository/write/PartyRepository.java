package com.playus.twpservice.domain.party.repository.write;

import com.playus.twpservice.domain.party.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Long> {
}
