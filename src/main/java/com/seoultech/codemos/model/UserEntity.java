package com.seoultech.codemos.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Integer level;
    private Integer experience;

    @ElementCollection
    private List<String> solvedProblems = new ArrayList<>(); // 값타입컬렉션

    @Enumerated(EnumType.STRING)  // 이 부분을 추가하여 enum을 문자열로 데이터베이스에 저장
    private Authority authority;
    @Builder
    public UserEntity(Long id, String email, String password, String nickname, Integer level, Integer experience, Authority authority) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.level = level;
        this.experience = experience;
        this.authority = authority;
    }

    public UserEntity(String loginId, String password, String nickname, Integer level, Integer experience, String profilePicURL){
        this.id = id;
        this.email = loginId;
        this.password = password;
        this.nickname = nickname;
        this.level = level;
        this.experience = experience;
        this.profilePicURL = profilePicURL;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<LeaderBoardEntity> leaderBoardEntries = new ArrayList<>();
}