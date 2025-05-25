package com.playus.twpservice.domain.common.security;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    public static final float DEFAULT_SCORE = 0.3f;

    private Long id;

    private String nickname;

    private String phoneNumber;

    private LocalDate birth;

    private Gender gender;

    private Role role;

    private AuthProvider authProvider;

    private boolean activated;

    private String thumbnailURL;

    private Float userScore;

    private LocalDateTime blockOff;

    private LocalDateTime createdAt;
}
