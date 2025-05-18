package com.playus.twpservice.domain.party.assertion;

import com.playus.twpservice.domain.party.entity.Party;
import org.springframework.util.Assert;

import java.util.Objects;

import static com.playus.twpservice.domain.party.exception.entity.PartyException.*;

public class PartyAssert extends Assert {

    public static void isLoginUserWriter(Long userId, Long writerId) {
        if (!Objects.equals(userId, writerId)) {
            throw new NotPartyWriterException("직관팟 작성자가 아니면 수정할 수 없습니다!");
        }
    }

    public static void isAppliableParty(Party party) {
        if (party.getCurrentParticipants() >= party.getMaximumParticipants()) {
            throw new ExceedPartyParticipantsException("직관팟 정원이 초과되었습니다!");
        }
    }
}
