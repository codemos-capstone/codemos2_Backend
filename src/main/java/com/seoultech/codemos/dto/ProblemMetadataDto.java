package com.seoultech.codemos.dto;

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
}