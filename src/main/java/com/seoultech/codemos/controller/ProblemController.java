package com.seoultech.codemos.controller;

import com.seoultech.codemos.dto.ProblemResponseDto;
import com.seoultech.codemos.service.ProblemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    // 일반 사용자용, 로그인 안해도 조회 가능?
    @GetMapping("/problems")
    public ResponseEntity<List<ProblemResponseDto>> getProblemList() {
        List<ProblemResponseDto> problemList = problemService.getProblemList();
        return ResponseEntity.ok(problemList);
    }

//    // 관리자용 엔드포인트
//    @PostMapping("/admin/problems")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//
//    @PutMapping("/admin/problems/{problemId}")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//
//    @DeleteMapping("/admin/problems/{problemId}")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
}