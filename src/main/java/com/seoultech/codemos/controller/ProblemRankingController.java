package com.seoultech.codemos.controller;

import com.seoultech.codemos.model.ProblemRanking;
import com.seoultech.codemos.repository.ProblemRankingRepository;
import com.seoultech.codemos.service.ProblemRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/problem-ranking")
public class ProblemRankingController {
    @Autowired
    private ProblemRankingService problemRankingService;

    @GetMapping("/problem")
    public ResponseEntity<Page<ProblemRanking>> getProblemRankings(
            @RequestParam String problemId,
            @RequestParam int page) {
        return ResponseEntity.ok(problemRankingService.getProblemRankingsByProblemId(problemId, page));
    }
}
