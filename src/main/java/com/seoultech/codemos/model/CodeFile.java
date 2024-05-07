package com.seoultech.codemos.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@Document(collection = "codeFiles")
public class CodeFile {
    @Id
    private String id;
    private Integer problemId;
    private String name;
    private String content;
    private String language;
//    @CreatedDate
//    private LocalDateTime createdAt;
//    @LastModifiedDate
//    private LocalDateTime updatedAt;
    private String createdAt;
    private String updatedAt;
    private String userId;
}