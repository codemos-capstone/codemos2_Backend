package com.seoultech.codemos.service;

import com.seoultech.codemos.dto.CodeFileRequestDto;
import com.seoultech.codemos.dto.CodeFileResponseDto;
import com.seoultech.codemos.model.CodeFile;
import com.seoultech.codemos.repository.CodeFileRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CodeFileService {

    private final CodeFileRepository codeFileRepository;
    private final MongoTemplate mongoTemplate;

    public CodeFileResponseDto createCodeFile(CodeFileRequestDto requestDto) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        CodeFile codeFile = CodeFile.builder()
                .problemId(requestDto.getProblemId())
                .name(requestDto.getName())
                .content(requestDto.getContent())
                .language(requestDto.getLanguage())
                .createdAt(requestDto.getCreatedAt())
                .userId(userId)
                .build();
        CodeFile savedCodeFile = codeFileRepository.save(codeFile);
        return mapToResponseDto(savedCodeFile);
    }

    public List<CodeFileResponseDto> getCodeFileList() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<CodeFile> codeFiles = codeFileRepository.findByUserId(userId);
        return codeFiles.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public List<CodeFileResponseDto> getProblemCodeFileList(Integer problemId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<CodeFile> codeFiles = codeFileRepository.findByUserIdAndProblemId(userId, problemId);
        return codeFiles.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public CodeFileResponseDto getCodeFileDetails(String fileId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        CodeFile codeFile = codeFileRepository.findByIdAndUserId(fileId, userId)
                .orElseThrow(() -> new RuntimeException("Code file not found"));
        return mapToResponseDto(codeFile);
    }

    public CodeFileResponseDto updateCodeFile(String fileId, CodeFileRequestDto requestDto) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        CodeFile codeFile = codeFileRepository.findByIdAndUserId(fileId, userId)
                .orElseThrow(() -> new RuntimeException("Code file not found"));
        codeFile.setContent(requestDto.getContent());
        codeFile.setUpdatedAt(requestDto.getUpdatedAt());
        CodeFile updatedCodeFile = codeFileRepository.save(codeFile);
        return mapToResponseDto(updatedCodeFile);
    }

    public void deleteCodeFile(String fileId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        CodeFile codeFile = codeFileRepository.findByIdAndUserId(fileId, userId)
                .orElseThrow(() -> new RuntimeException("Code file not found"));
        codeFileRepository.delete(codeFile);
    }

    private CodeFileResponseDto mapToResponseDto(CodeFile codeFile) {
        return new CodeFileResponseDto(
                codeFile.getId(),
                codeFile.getProblemId(),
                codeFile.getName(),
                codeFile.getContent(),
                codeFile.getLanguage(),
                codeFile.getCreatedAt(),
                codeFile.getUpdatedAt(),
                codeFile.getUserId()
        );
    }
}
