package com.playus.twpservice.domain.party.assertion;

import org.springframework.util.Assert;

import java.util.Objects;

import static com.playus.twpservice.domain.party.exception.entity.PartyException.*;

public class PartyAssert extends Assert {

    public static void isLoginUserWriter(Long userId, Long writerId) {
        if (!Objects.equals(userId, writerId)) {
            throw new NotPartyWriterException("직관팟 작성자가 아니면 수정할 수 없습니다!");
        }
    }
}
