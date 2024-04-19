package com.seoultech.codemos.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private String profilePicURL;
    private SimpleGrantedAuthority authority;
    @Builder
    public UserEntity(Long id, String email, String password, String nickname) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public UserEntity(String loginId, String password, String nickname, String profilePicURL){
        this.id = id;
        this.email = loginId;
        this.password = password;
        this.nickname = nickname;
        this.profilePicURL = profilePicURL;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<LeaderBoardEntity> leaderBoardEntries = new ArrayList<>();
}