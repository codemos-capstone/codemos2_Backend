package com.seoultech.codemos.controller;

import com.seoultech.codemos.dto.JudgeCodeDTO;
import com.seoultech.codemos.dto.JudgeResultResponseDTO;
import com.seoultech.codemos.jwt.TokenProvider;
import com.seoultech.codemos.service.JudgeService;
import com.seoultech.codemos.service.ProblemService;
import com.seoultech.codemos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/judge")
@RequiredArgsConstructor
public class JudgeController {

    private final JudgeService judgeService;
    private final UserService userService;
    private final ProblemService problemService;

    @PostMapping("problem/{problemId}/score")
    public ResponseEntity<JudgeResultResponseDTO> execute(@PathVariable Integer problemId, @RequestBody JudgeCodeDTO requestDto) {//,

        JudgeResultResponseDTO responseDTO = judgeService.judgeCode(problemId, requestDto.getCode());

        if (responseDTO != null && responseDTO.getScore() > 0) {
            handleProblemSolved(problemId, requestDto.getCode());
        }

        return ResponseEntity.ok(responseDTO);
    }

    private void handleProblemSolved(Integer problemId, String code) {
        userService.updateSolvedProblem(problemId);

        problemService.updateSolvedUsers(problemId);
        problemService.updateProblemRanking(problemId, code);
    }
}