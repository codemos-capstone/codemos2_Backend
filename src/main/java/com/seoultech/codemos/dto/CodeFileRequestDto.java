package com.seoultech.codemos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CodeFileRequestDto {
    private Integer problemId;
    private String name;
    private String content;
    private String language;
    private String CreatedAt;
    private String UpdatedAt;
}