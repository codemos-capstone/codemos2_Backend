package com.seoultech.codemos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Entity
public class RankingEntity {

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToOne
    @JoinColumn(name = "code_id")
    private CodeEntity code;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int score;
    private String time;
    private int leaderBoardId;
    private String nickname;
    private String email;


    public RankingEntity() {
    }
}
