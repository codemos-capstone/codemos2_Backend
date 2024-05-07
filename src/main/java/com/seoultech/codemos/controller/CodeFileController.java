package com.seoultech.codemos.controller;

import com.seoultech.codemos.dto.CodeFileRequestDto;
import com.seoultech.codemos.dto.CodeFileResponseDto;
import com.seoultech.codemos.dto.JudgeResultResponseDTO;
import com.seoultech.codemos.service.CodeFileService;
import com.seoultech.codemos.service.JudgeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/code-file")
@RequiredArgsConstructor
public class CodeFileController {

    private final CodeFileService codeFileService;
    private final JudgeService judgeService;

    @PostMapping
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CodeFileResponseDto> createCodeFile(@RequestBody CodeFileRequestDto requestDto) {
        CodeFileResponseDto responseDto = codeFileService.createCodeFile(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<CodeFileResponseDto>> getCodeFileList() {
        List<CodeFileResponseDto> responseDtoList = codeFileService.getCodeFileList();
        return ResponseEntity.ok(responseDtoList);
    }

    @GetMapping("/{fileId}")
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CodeFileResponseDto> getCodeFileDetails(@PathVariable String fileId) {
        CodeFileResponseDto responseDto = codeFileService.getCodeFileDetails(fileId);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{fileId}")
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CodeFileResponseDto> updateCodeFile(@PathVariable String fileId, @RequestBody CodeFileRequestDto requestDto) {
        CodeFileResponseDto responseDto = codeFileService.updateCodeFile(fileId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{fileId}")
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteCodeFile(@PathVariable String fileId) {
        codeFileService.deleteCodeFile(fileId);
        return ResponseEntity.noContent().build();
    }

}
