package com.seoultech.codemos.repository;

import static com.seoultech.codemos.service.ProblemService.USER_PROBLEM_START_NUMBER;

import com.seoultech.codemos.model.Problem;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemRepository extends MongoRepository<Problem, String> {
    List<Problem> findByUserDefinedFalse();
    List<Problem> findByUserDefinedTrue();

    @Query("{ 'isUserDefined': true, 'problemNumber': { $exists: true } }")
    List<Problem> findUserDefinedProblemsWithProblemNumber(Sort sort);

    default int findMaxUserProblemNumber() {
        List<Problem> problems = findUserDefinedProblemsWithProblemNumber(Sort.by(Sort.Direction.DESC, "problemNumber"));
        if (!problems.isEmpty()) {
            return problems.get(0).getProblemNumber();
        }
        return USER_PROBLEM_START_NUMBER - 1;
    }
}