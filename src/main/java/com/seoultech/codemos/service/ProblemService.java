package com.seoultech.codemos.service;

import com.seoultech.codemos.dto.ProblemRequestDto;
import com.seoultech.codemos.dto.ProblemResponseDto;
import com.seoultech.codemos.model.Problem;
import com.seoultech.codemos.repository.ProblemRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemRepository problemRepository;

    public List<ProblemResponseDto> getProblemList() {
        List<Problem> problems = problemRepository.findAll();
        return problems.stream()
                .map(this::mapToProblemResponse)
                .collect(Collectors.toList());
    }

    public ProblemResponseDto createProblem(ProblemRequestDto requestDto) {
        Problem problem = mapToProblem(requestDto);
        Problem savedProblem = problemRepository.save(problem);
        return mapToProblemResponse(savedProblem);
    }

    private Problem mapToProblem(ProblemRequestDto requestDto) {
        return Problem.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .timeLimit(requestDto.getTimeLimit())
                .fuelLimit(requestDto.getFuelLimit())
                .initialX(requestDto.getInitialX())
                .initialY(requestDto.getInitialY())
                .initialAngle(requestDto.getInitialAngle())
                .initialVelocityX(requestDto.getInitialVelocityX())
                .initialVelocityY(requestDto.getInitialVelocityY())
                .restrictedMethods(requestDto.getRestrictedMethods())
                .isUserDefined(requestDto.isUserDefined())
                .build();
    }

    private ProblemResponseDto mapToProblemResponse(Problem problem) {
        return ProblemResponseDto.builder()
                .id(problem.getId())
                .title(problem.getTitle())
                .description(problem.getDescription())
                .timeLimit(problem.getTimeLimit())
                .fuelLimit(problem.getFuelLimit())
                .initialX(problem.getInitialX())
                .initialY(problem.getInitialY())
                .initialAngle(problem.getInitialAngle())
                .initialVelocityX(problem.getInitialVelocityX())
                .initialVelocityY(problem.getInitialVelocityY())
                .restrictedMethods(problem.getRestrictedMethods())
                .isUserDefined(problem.isUserDefined())
                .build();
    }
}
