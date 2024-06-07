package com.seoultech.codemos.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JudgeResultResponseDTO {
    float score;
    float fuel;
    int time;
    int bytes;
    float angle;
    float velX;
    float velY;
}