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
    private Integer problemNumber;
    private String title;
    private String description;
    private Integer difficulty;
    private double timeLimit;
    private double fuelLimit;
    private double initialX;
    private double initialY;
    private double initialAngle;
    private double initialVelocityX;
    private double initialVelocityY;
    private double rotationVelocity;
    private List<String> restrictedMethods;
    private boolean isUserDefined;
    private String userId;
    private List<String> solvedUsers;
    private List<String> tags;
}