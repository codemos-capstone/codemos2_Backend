package com.seoultech.codemos.service;

import com.seoultech.codemos.dto.JudgeResultResponseDTO;
import com.seoultech.codemos.dto.ProblemMetadataDto;
import com.seoultech.codemos.dto.ProblemRequestDto;
import com.seoultech.codemos.dto.ProblemResponseDto;
import com.seoultech.codemos.model.Problem;
import com.seoultech.codemos.model.ProblemRanking;
import com.seoultech.codemos.model.UserEntity;
import com.seoultech.codemos.repository.ProblemRankingRepository;
import com.seoultech.codemos.repository.ProblemRepository;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final ProblemRankingRepository problemRankingRepository;
    private final AuthService authService;
    public static final int USER_PROBLEM_START_NUMBER = 10000;

    public List<ProblemMetadataDto> getProblemList() {
        List<Problem> problems = problemRepository.findAll();
        return problems.stream()
                .map(this::mapToProblemMetadata)
                .collect(Collectors.toList());
    }

    public List<ProblemMetadataDto> getOfficialProblemList() {
        List<Problem> problems = problemRepository.findByIsUserDefinedFalse();
        return problems.stream()
                .map(this::mapToProblemMetadata)
                .collect(Collectors.toList());
    }

    public List<ProblemMetadataDto> getUserProblemList() {
        List<Problem> problems = problemRepository.findByIsUserDefinedTrue();
        return problems.stream()
                .map(this::mapToProblemMetadata)
                .collect(Collectors.toList());
    }

    private ProblemMetadataDto mapToProblemMetadata(Problem problem) {
        return ProblemMetadataDto.builder()
                .id(problem.getId())
                .problemNumber(problem.getProblemNumber())
                .title(problem.getTitle())
                .description(problem.getDescription())
                .difficulty(problem.getDifficulty())
                .isUserDefined(problem.isUserDefined())
                .userId(problem.getUserId())
                .tags(problem.getTags())
                .build();
    }

    public ProblemResponseDto createUserProblem(ProblemRequestDto requestDto) {
        UserEntity user = authService.getCurrentUser();
        Problem problem = mapToProblem(requestDto);
        problem.setUserId(user.getNickname());

        int maxUserProblemNumber = problemRepository.findMaxUserProblemNumber();
        problem.setProblemNumber(maxUserProblemNumber + 1);

        Problem savedProblem = problemRepository.save(problem);
        return mapToProblemResponse(savedProblem);
    }

    public ProblemResponseDto updateUserProblem(int problemId, ProblemRequestDto requestDto) {
        UserEntity user = authService.getCurrentUser();
        Problem problem = problemRepository.findByProblemNumber(problemId)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + problemId));

        if (!problem.isUserDefined() || !problem.getUserId().equals(user.getNickname())) {
            throw new IllegalArgumentException("No auth");
        }

        updateProblemFields(problem, requestDto);

        Problem updatedProblem = problemRepository.save(problem);

        return mapToProblemResponse(updatedProblem);
    }

    public void deleteUserProblem(int problemId) {
        UserEntity user = authService.getCurrentUser();
        Problem problem = problemRepository.findByProblemNumber(problemId)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + problemId));

        if (!problem.isUserDefined() || !problem.getUserId().equals(user.getNickname())) {
            throw new IllegalArgumentException("No auth");
        }

        problemRepository.delete(problem);
    }

    public ProblemResponseDto getProblemDetails(int problemId) {
        Problem problem = problemRepository.findByProblemNumber(problemId)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + problemId));
        return mapToProblemResponse(problem);
    }

    public ProblemResponseDto createProblem(ProblemRequestDto requestDto) {
        Problem problem = mapToProblem(requestDto);
        if (requestDto.getProblemNumber() != null) {
            problem.setProblemNumber(requestDto.getProblemNumber());
        }

        if (requestDto.getDifficulty() != null) {
            problem.setDifficulty(requestDto.getDifficulty());
        } else {
            problem.setDifficulty(0);
        }

        Problem savedProblem = problemRepository.save(problem);
        return mapToProblemResponse(savedProblem);
    }

    public void deleteProblem(int problemId) {
        Problem problem = problemRepository.findByProblemNumber(problemId)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + problemId));
        problemRepository.delete(problem);
    }

    public ProblemResponseDto updateProblem(int problemId, ProblemRequestDto requestDto) {
        Problem problem = problemRepository.findByProblemNumber(problemId)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + problemId));

        updateProblemFields(problem, requestDto);

        Problem updatedProblem = problemRepository.save(problem);
        return mapToProblemResponse(updatedProblem);
    }

    public void updateSolvedUsers(Integer problemId) {
        UserEntity user = authService.getCurrentUser();
        String userId = user.getNickname();

        Problem problem = problemRepository.findByProblemNumber(problemId)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + problemId));

        if (!problem.getSolvedUsers().contains(userId)) {
            problem.getSolvedUsers().add(userId);
            problemRepository.save(problem);
        }
    }

    public void updateProblemRanking(Integer problemId, String code, JudgeResultResponseDTO judgeResult) {
        UserEntity user = authService.getCurrentUser();

        ProblemRanking problemRanking = problemRankingRepository.findByProblemIdAndUserId(problemId.toString(), user.getId().toString())
                .orElse(new ProblemRanking());

        Integer codeByteSize = calculateCodeByteSize(code);
        problemRanking.setProblemId(problemId.toString());
        problemRanking.setUserId(user.getEmail());
        problemRanking.setScore(judgeResult.getScore());
        problemRanking.setFuel(judgeResult.getFuel());
        problemRanking.setTime(judgeResult.getTime());
        problemRanking.setCodeByteSize(codeByteSize);
        problemRanking.setCode(code);

        problemRankingRepository.save(problemRanking);
    }

    private Integer calculateCodeByteSize(String code) {
        if (code == null) {
            return 0;
        }

        byte[] bytes = code.getBytes(StandardCharsets.UTF_8);
        return bytes.length;
    }

    private void updateProblemFields(Problem problem, ProblemRequestDto requestDto) {
        problem.setTitle(requestDto.getTitle());
        problem.setDescription(requestDto.getDescription());
        problem.setTimeLimit(requestDto.getTimeLimit());
        problem.setFuelLimit(requestDto.getFuelLimit());
        problem.setInitialX(requestDto.getInitialX());
        problem.setInitialY(requestDto.getInitialY());
        problem.setInitialAngle(requestDto.getInitialAngle());
        problem.setInitialVelocityX(requestDto.getInitialVelocityX());
        problem.setInitialVelocityY(requestDto.getInitialVelocityY());
        problem.setRotationVelocity(requestDto.getRotationVelocity());
        problem.setRestrictedMethods(requestDto.getRestrictedMethods());
    }

    private Problem mapToProblem(ProblemRequestDto requestDto) {
        return Problem.builder()
                .problemNumber(requestDto.getProblemNumber())
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .timeLimit(requestDto.getTimeLimit())
                .fuelLimit(requestDto.getFuelLimit())
                .initialX(requestDto.getInitialX())
                .initialY(requestDto.getInitialY())
                .initialAngle(requestDto.getInitialAngle())
                .initialVelocityX(requestDto.getInitialVelocityX())
                .initialVelocityY(requestDto.getInitialVelocityY())
                .rotationVelocity(requestDto.getRotationVelocity())
                .restrictedMethods(requestDto.getRestrictedMethods())
                .isUserDefined(requestDto.isUserDefined())
                .solvedUsers(new ArrayList<>())
                .tags(requestDto.getTags())
                .build();
    }

    private ProblemResponseDto mapToProblemResponse(Problem problem) {
        return ProblemResponseDto.builder()
                .id(problem.getId())
                .problemNumber(problem.getProblemNumber())
                .title(problem.getTitle())
                .description(problem.getDescription())
                .timeLimit(problem.getTimeLimit())
                .fuelLimit(problem.getFuelLimit())
                .initialX(problem.getInitialX())
                .initialY(problem.getInitialY())
                .initialAngle(problem.getInitialAngle())
                .initialVelocityX(problem.getInitialVelocityX())
                .initialVelocityY(problem.getInitialVelocityY())
                .rotationVelocity(problem.getRotationVelocity())
                .restrictedMethods(problem.getRestrictedMethods())
                .isUserDefined(problem.isUserDefined())
                .solvedUsers(problem.getSolvedUsers())
                .userId(problem.getUserId())
                .tags(problem.getTags())
                .build();
    }
}
