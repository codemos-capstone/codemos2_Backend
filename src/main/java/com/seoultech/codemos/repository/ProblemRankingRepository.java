package com.seoultech.codemos.repository;

import com.seoultech.codemos.model.ProblemRanking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProblemRankingRepository extends MongoRepository<ProblemRanking, String> {
    Optional<ProblemRanking> findByProblemIdAndUserId(String problemId, String userId);
}