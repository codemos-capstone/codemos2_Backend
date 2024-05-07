package com.seoultech.codemos.controller;

import com.seoultech.codemos.dto.CodeFileRequestDto;
import com.seoultech.codemos.dto.CodeFileResponseDto;
import com.seoultech.codemos.service.CodeFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/code-file")
@RequiredArgsConstructor
public class CodeFileController {

    private final CodeFileService codeFileService;

    @PostMapping
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<CodeFileResponseDto> createCodeFile(@RequestBody CodeFileRequestDto requestDto) {
        CodeFileResponseDto responseDto = codeFileService.createCodeFile(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
