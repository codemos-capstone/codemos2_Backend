package com.seoultech.codemos.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Builder
@Document(collection = "problems")
public class Problem {
    @Id
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