package com.playus.twpservice.domain.party.assertion;

import com.playus.twpservice.domain.common.security.Gender;
import com.playus.twpservice.domain.party.entity.Party;
import com.playus.twpservice.domain.party.enums.PartyAgeGroup;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

import static com.playus.twpservice.domain.party.exception.entity.PartyException.*;

public class PartyAssert extends Assert {

    // 400
    public static void isLoginUserWriter(Long userId, Long writerId, String message) {
        if (!Objects.equals(userId, writerId)) {
            throw new NotPartyWriterException(message);
        }
    }

    // 400
    public static void isParticipatedPartyAsWriter(Long userId, Long writerId, String message) {
        if (Objects.equals(userId, writerId)) {
            throw new NotPartyWriterException(message);
        }
    }

    // 409
    public static void isAppliableParty(Party party, List<PartyAgeGroup> partyAgeGroupList, Gender userGender, PartyAgeGroup userAgeGroup) {
        if (party.getCurrentParticipants() >= party.getMaximumParticipants()) {
            throw new ExceedPartyParticipantsException("직관팟 정원이 초과되었습니다!");
        }

        // 400
        if (userGender != Gender.UNDEFINED && !party.getPartyGender().equals(userGender.toPartyGender())) {
            throw new NotAllowedPartyConditionException("직관팟 성별이 맞지 않습니다!");
        }

        // 400
        if (!partyAgeGroupList.contains(userAgeGroup)) {
            throw new NotAllowedPartyConditionException("직관팟 나이가 맞지 않습니다!");
        }
    }
}
