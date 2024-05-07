package com.seoultech.codemos.controller;

import com.seoultech.codemos.dto.JudgeCodeDTO;
import com.seoultech.codemos.dto.JudgeResultResponseDTO;
import com.seoultech.codemos.jwt.TokenProvider;
import com.seoultech.codemos.service.JudgeService;
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

    private final TokenProvider tokenProvider;
    private final JudgeService judgeService;

    @PostMapping("problem/{problemId}/execute")
    public ResponseEntity<JudgeResultResponseDTO> execute(@PathVariable Integer problemId, @RequestBody JudgeCodeDTO requestDto) {//,
        //@RequestHeader(value = "Authorization") String token) {
//        token = token.substring(7);
//        String username = tokenProvider.getUsernameFromToken(token); // 사용자 검증

        JudgeResultResponseDTO responseDTO = judgeService.judgeCode(problemId, requestDto.getCode());
//        System.out.println("responseDTO = " + responseDTO);
        return ResponseEntity.ok(responseDTO);
    }
}