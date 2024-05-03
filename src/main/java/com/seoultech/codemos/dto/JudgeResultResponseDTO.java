package com.seoultech.codemos.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JudgeResultResponseDTO {
    float score;
    float fuel;
    int time;
}