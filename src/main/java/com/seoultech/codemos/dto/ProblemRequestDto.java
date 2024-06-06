package com.seoultech.codemos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemRequestDto {
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
    private List<String> restrictedMethods;
    private boolean isUserDefined;
    private List<String> tags;
}