package com.seoultech.codemos.dto;

import com.seoultech.codemos.model.Authority;
import com.seoultech.codemos.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDTO {
    private String email;
    private String password;
    private String nickname;

    public UserEntity toUser(PasswordEncoder passwordEncoder) {
        return UserEntity.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .level(0)
                .experience(0)
                .authority(Authority.ROLE_USER)
                .build();
    }

}