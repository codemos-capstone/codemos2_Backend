package com.seoultech.codemos.service;

import com.seoultech.codemos.model.ProblemRanking;
import com.seoultech.codemos.model.RankingEntity;
import com.seoultech.codemos.repository.ProblemRankingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemRankingService {

    @Autowired
    private ProblemRankingRepository problemRankingRepository;
    public Page<ProblemRanking> getProblemRankingsByProblemId(String problemId, int page) {

        PageRequest pageable = PageRequest.of(page, 20);
        return problemRankingRepository.findByProblemIdOrderByScoreDesc(problemId, pageable);
    }
}
