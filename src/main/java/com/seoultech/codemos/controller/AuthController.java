package com.seoultech.codemos.controller;

import com.seoultech.codemos.dto.LoginRequestDTO;
import com.seoultech.codemos.dto.TokenDto;
import com.seoultech.codemos.dto.UserRequestDTO;
import com.seoultech.codemos.dto.UserResponseDTO;
import com.seoultech.codemos.jwt.TokenProvider;
import com.seoultech.codemos.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final TokenProvider tokenProvider;
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
    @PostMapping("/changepwd")
    public void changePwd(){}
    @GetMapping("/findLoginId")
    public void FindLoginId(){}
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