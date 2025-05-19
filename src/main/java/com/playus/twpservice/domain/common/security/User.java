package com.playus.twpservice.domain.common.security;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
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

    @Builder
    private User(String nickname,String phoneNumber, LocalDate birth, Gender gender, Role role, AuthProvider authProvider, boolean activated, LocalDateTime blockOff, String thumbnailURL, Float userScore) {
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.birth = birth;
        this.gender = gender;
        this.role = role;
        this.authProvider = authProvider;
        this.activated = activated;
        this.blockOff = blockOff;
        this.thumbnailURL = thumbnailURL;
        this.userScore = userScore;
    }

    public static User create(String nickname,String phoneNumber, LocalDate birth, Gender gender, Role role, AuthProvider authProvider, String thumbnailURL) {
        return User.builder()
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .birth(birth)
                .gender(gender)
                .role(role)
                .authProvider(authProvider)
                .activated(true)
                .blockOff(null)
                .thumbnailURL(thumbnailURL)
                .userScore(DEFAULT_SCORE)
                .build();
    }
}
