package com.seoultech.codemos.service;

import com.seoultech.codemos.dto.LoginRequestDTO;
import com.seoultech.codemos.dto.TokenDto;
import com.seoultech.codemos.dto.UserRequestDTO;
import com.seoultech.codemos.dto.UserResponseDTO;
import com.seoultech.codemos.jwt.TokenProvider;
import com.seoultech.codemos.model.UserEntity;
import com.seoultech.codemos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthenticationManagerBuilder managerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public UserResponseDTO signup(UserRequestDTO requestDto) {
        System.out.println("로그인 시도 이메일: "+ requestDto.getEmail());
        System.out.println("로그인 시도 닉네임: "+ requestDto.getNickname());
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 이메일입니다");
        }
        if (userRepository.existsByNickname(requestDto.getNickname())) {
            throw new RuntimeException("이미 가입되어 있는 닉네임입니다");
        }
        UserEntity user = requestDto.toUser(passwordEncoder);
        return UserResponseDTO.of(userRepository.save(user));
    }

    public TokenDto login(LoginRequestDTO requestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
        return tokenProvider.generateTokenDto(authentication);
    }
    public Authentication getAuthentication(String email) {
        return new UsernamePasswordAuthenticationToken(email, null);
    }

}