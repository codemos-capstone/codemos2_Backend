package com.seoultech.codemos.repository;

import com.seoultech.codemos.model.CodeFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodeFileRepository extends MongoRepository<CodeFile, String> {
    List<CodeFile> findByUserId(String userId);
    Optional<CodeFile> findByIdAndUserId(String id, String userId);
}