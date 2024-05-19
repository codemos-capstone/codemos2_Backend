package com.seoultech.codemos.repository;

import static com.seoultech.codemos.service.ProblemService.USER_PROBLEM_START_NUMBER;

import com.seoultech.codemos.model.Problem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemRepository extends MongoRepository<Problem, String> {
    List<Problem> findByIsUserDefinedFalse();
    List<Problem> findByIsUserDefinedTrue();
    Optional<Problem> findByProblemNumber(int problemNumber);

    @Query("{ 'isUserDefined': true, 'problemNumber': { $exists: true } }")
    List<Problem> findIsUserDefinedProblemsWithProblemNumber(Sort sort);

    default int findMaxUserProblemNumber() {
        List<Problem> problems = findIsUserDefinedProblemsWithProblemNumber(Sort.by(Sort.Direction.DESC, "problemNumber"));
        if (!problems.isEmpty()) {
            return problems.get(0).getProblemNumber();
        }
        return USER_PROBLEM_START_NUMBER - 1;
    }

}