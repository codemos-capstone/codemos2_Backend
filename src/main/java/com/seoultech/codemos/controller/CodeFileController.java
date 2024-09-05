package com.seoultech.codemos.controller;

import com.seoultech.codemos.dto.CodeFileRequestDto;
import com.seoultech.codemos.dto.CodeFileResponseDto;
import com.seoultech.codemos.dto.JudgeResultResponseDTO;
import com.seoultech.codemos.dto.ProblemResponseDto;
import com.seoultech.codemos.service.CodeFileService;
import com.seoultech.codemos.service.JudgeService;
import com.seoultech.codemos.service.ProblemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
@EnableMethodSecurity
public class CodeFileController {

    private final CodeFileService codeFileService;
    private final JudgeService judgeService;
    private final ProblemService problemService;


    @PostMapping
    @PreAuthorize("hasAuthority('OAUTH2_USER') or hasAuthority('ROLE_USER')")
    public ResponseEntity<CodeFileResponseDto> createCodeFile(@RequestBody CodeFileRequestDto requestDto) {
        CodeFileResponseDto responseDto = codeFileService.createCodeFile(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('OAUTH2_USER') or hasAuthority('ROLE_USER')")
    public ResponseEntity<List<CodeFileResponseDto>> getCodeFileList() {
        System.out.println("codeFileService = " + codeFileService);
        List<CodeFileResponseDto> responseDtoList = codeFileService.getCodeFileList();
        return ResponseEntity.ok(responseDtoList);
    }

    @GetMapping("/problem/{problemId}")
    //@PreAuthorize("hasAuthority('OAUTH2_USER') or hasAuthority('ROLE_USER')")
    public ResponseEntity<List<CodeFileResponseDto>> getProblemCodeFileList(@PathVariable Integer problemId) {
        List<CodeFileResponseDto> responseDtoList = codeFileService.getProblemCodeFileList(problemId);
        return ResponseEntity.ok(responseDtoList);
    }

    @GetMapping("/{fileId}")
    @PreAuthorize("hasAuthority('OAUTH2_USER') or hasAuthority('ROLE_USER')")
    public ResponseEntity<CodeFileResponseDto> getCodeFileDetails(@PathVariable String fileId) {
        CodeFileResponseDto responseDto = codeFileService.getCodeFileDetails(fileId);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{fileId}")
    @PreAuthorize("hasAuthority('OAUTH2_USER') or hasAuthority('ROLE_USER')")
    public ResponseEntity<CodeFileResponseDto> updateCodeFile(@PathVariable String fileId, @RequestBody CodeFileRequestDto requestDto) {
        CodeFileResponseDto responseDto = codeFileService.updateCodeFile(fileId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{fileId}")
    @PreAuthorize("hasAuthority('OAUTH2_USER') or hasAuthority('ROLE_USER')")
    public ResponseEntity<Void> deleteCodeFile(@PathVariable String fileId) {
        codeFileService.deleteCodeFile(fileId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/problem/{problemId}/{fileId}/execute")
    @PreAuthorize("hasAuthority('OAUTH2_USER') or hasAuthority('ROLE_USER')")
    public ResponseEntity<JudgeResultResponseDTO> executeCodeFile(@PathVariable Integer problemId, @PathVariable String fileId) {
        CodeFileResponseDto codeFileResponseDto = codeFileService.getCodeFileDetails(fileId);
        String code = codeFileResponseDto.getContent();

        ProblemResponseDto problem = problemService.getProblemDetails(problemId);
        JudgeResultResponseDTO judgeResult = judgeService.judgeCode(problem, code);

        if (judgeResult != null) {
            return ResponseEntity.ok(judgeResult);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
