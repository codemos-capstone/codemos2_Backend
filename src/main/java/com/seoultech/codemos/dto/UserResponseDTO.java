package com.seoultech.codemos.dto;

import com.seoultech.codemos.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {
    private String email;
    private String nickname;
    private String profilePicURL;
    private boolean state;

    public static UserResponseDTO of(UserEntity user) {
        return UserResponseDTO.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profilePicURL(user.getProfilePicURL())
                .build();
    }
}