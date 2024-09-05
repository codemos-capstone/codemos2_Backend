package com.seoultech.codemos.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "problem_rankings")
public class ProblemRanking {
    @Id
    private String id;
    private String problemId;
    private String userId;
    private float score;
    private float fuel;
    private Integer time;
    private Integer codeByteSize;

    private String code;

    @Builder
    public ProblemRanking(String id, String problemId, String userId, Float score, Integer codeByteSize) {
        this.id = id;
        this.problemId = problemId;
        this.userId = userId;
        this.score = score;
        this.codeByteSize = codeByteSize;
    }
}