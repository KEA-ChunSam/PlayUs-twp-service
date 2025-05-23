package com.playus.twpservice.domain.common.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class CustomOAuth2User implements OAuth2User {

    private final UserDto userDto;
    private final String accessToken;

    public CustomOAuth2User(UserDto userDto , String accessToken) {
        this.userDto = userDto;
        this.accessToken = accessToken;
    }

    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", userDto.getId());
        attributes.put("nickname", userDto.getNickname());
        attributes.put("birth", userDto.getBirth());
        attributes.put("gender", userDto.getGender());
        attributes.put("role", userDto.getRole());
        attributes.put("authProvider", userDto.getAuthProvider());
        attributes.put("activated", userDto.isActivated());
        attributes.put("userScore", userDto.getUserScore());
        attributes.put("thumbnailURL", userDto.getThumbnailURL());

        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of((GrantedAuthority) () -> userDto.getRole().name());
    }

    @Override
    public String getName() {
        return String.valueOf(userDto.getId()); // 이거빼면 OAuth2User implements 에서 에러
    }

    public Long getId() {
        return Long.parseLong(String.valueOf(userDto.getId()));
    }
}
