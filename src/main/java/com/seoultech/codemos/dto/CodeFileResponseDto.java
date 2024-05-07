package com.seoultech.codemos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CodeFileResponseDto {
    private String id;
    private String name;
    private String content;
    private String language;
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
    private String createdAt;
    private String updatedAt;
    private String userId;
}