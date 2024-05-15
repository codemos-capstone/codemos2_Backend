package com.seoultech.codemos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemResponseDto {
    private String id;
    private String title;
    private String description;
    private double timeLimit;
    private double fuelLimit;
    private double initialX;
    private double initialY;
    private double initialAngle;
    private double initialVelocityX;
    private double initialVelocityY;
    private List<String> restrictedMethods;
    private boolean isUserDefined;
}