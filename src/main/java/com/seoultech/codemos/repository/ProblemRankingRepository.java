package com.seoultech.codemos.repository;

import com.seoultech.codemos.model.ProblemRanking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProblemRankingRepository extends MongoRepository<ProblemRanking, String> {
    Optional<ProblemRanking> findByProblemIdAndUserId(String problemId, String userId);
    List<ProblemRanking> findAll();
    // problemId로 조회하고 score를 기준으로 정렬 후 페이징
    Page<ProblemRanking> findByProblemIdOrderByScoreDesc(String problemId, Pageable pageable);
}