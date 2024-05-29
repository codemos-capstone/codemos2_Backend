package com.seoultech.codemos.model;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@Document(collection = "problem_rankings")
public class ProblemRanking {
    @Id
    private String id;
    private String problemId;
    private String userId;
    private Integer score;
    private Integer codeByteSize;
}