package com.seoultech.codemos.controller;

import com.seoultech.codemos.dto.*;
import com.seoultech.codemos.jwt.TokenProvider;
import com.seoultech.codemos.service.AuthService;
import com.seoultech.codemos.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final TokenProvider tokenProvider;
    private final EmailService emailService;
    @PostMapping("/sign")
    public ResponseEntity<?>  CreateUserInfo(@RequestBody UserRequestDTO requestDto) {
        try {
            UserResponseDTO responseDto = authService.signup(requestDto);
            return ResponseEntity.ok(responseDto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/sign")
    public void deleteUserInfo(){}
    @PutMapping("/sign")
    public void updateUserInfo(){}
    @PostMapping("/change-pwd")
    public ResponseEntity changePwd(@RequestParam(name = "email") String email){
        EmailDTO emailMessage = EmailDTO.builder()
                .to(email)
                .subject("테스트메일")
                .message("비밀번호 재설정을 위한 이메일입니다.")
                .build();
        emailService.sendMail(emailMessage);
        return new ResponseEntity(HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginRequestDTO requestDto) {

        return ResponseEntity.ok(authService.login(requestDto));
    }



    @GetMapping("/verify-token")
    public ResponseEntity<?> verifyToken(@RequestHeader(value="Authorization") String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            boolean isValid = tokenProvider.validateToken(token);
            if (isValid) {
                return ResponseEntity.ok("Token is valid.");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
    }
}