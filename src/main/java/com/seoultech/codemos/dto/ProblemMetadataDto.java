package com.seoultech.codemos.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemMetadataDto {
    private String id;
    private int problemNumber;
    private String title;
    private String description;
    private Integer difficulty;
    private boolean isUserDefined;
    private String userId;
    private List<String> tags;
}