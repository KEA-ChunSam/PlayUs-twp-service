package com.playus.twpservice.domain.party.enums;

import com.playus.twpservice.domain.party.exception.document.PartyJoinDocumentException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PartyJoinRequestStatus {

    WAIT("신청중"), REFUSE("승인 거부됨"), ACCEPT("채팅방 입장!");

    private String message;

    public static void throwIfAlreadyAppliedToParty(PartyJoinRequestStatus status) {

        if (status == null) return;

        if (status == PartyJoinRequestStatus.REFUSE) {
            throw new PartyJoinDocumentException.RefusedApplyUserException("신청이 거절되었으면 다시 지원할 수 없습니다!");
        }

        else {
            throw new PartyJoinDocumentException.DuplicateApplyException("이미 가입된 직관팟입니다!");
        }
    }
}
