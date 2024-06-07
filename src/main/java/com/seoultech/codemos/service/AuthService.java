package com.seoultech.codemos.service;

import com.seoultech.codemos.config.SecurityUtil;
import com.seoultech.codemos.dto.ChangePasswordRequestDTO;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthenticationManagerBuilder managerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public UserResponseDTO signup(UserRequestDTO requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RuntimeException("This email is already registered");
        }
        if (userRepository.existsByNickname(requestDto.getNickname())) {
            throw new RuntimeException("This nickname is already registered.");
        }
        if (requestDto.getPassword() != null) {
            String password = requestDto.getPassword();

            if (!isValidLength(password)) {
                throw new RuntimeException("The password must be between 8 and 10 characters.");
            }

            if (!isValidComplexity(password)) {
                throw new RuntimeException(
                        "Password must contain upper and lower case letters, numbers, and special characters." +
                                "However, the password cannot contain (' \\ \" ; --)."
                );
            }

            if (containsSqlInjectionChars(password)) {
                throw new RuntimeException("Password cannot contain (' \\ \"  ; --).");
            }

        } else {
            throw new IllegalArgumentException("Please Enter your Password.");
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

    public void resetPassword(String resetPwdToken, ChangePasswordRequestDTO requestDTO) {
        if(!tokenProvider.validateToken(resetPwdToken)){
            throw new RuntimeException("썩은토큰");
        }
        String email = tokenProvider.getUsernameFromToken(resetPwdToken);
        System.out.println("email = " + email);
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("누구세요"));

        user.setPassword(passwordEncoder.encode(requestDTO.getNewPassword()));
        userRepository.save(user);

        tokenProvider.expireToken(resetPwdToken);
    }

    public UserEntity getCurrentUser() {
//        Long userId = SecurityUtil.getCurrentMemberId();
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 10;

    // 비밀번호 복잡도 검사: 영문 대소문자, 숫자, 특수문자 포함
    private static final Pattern COMPLEXITY_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-\\[\\]{};':\"\\\\|,.<>\\/?]).*$");

    // SQL 인젝션 관련 문자 검사: ' " ; --와 같은 문자를 포함하지 않아야 함
    private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile("[\"';\\-\\-]");


    public static boolean isValidLength(String password) {
        return password != null && password.length() >= MIN_LENGTH && password.length() <= MAX_LENGTH;
    }

    public static boolean isValidComplexity(String password) {
        return password != null && COMPLEXITY_PATTERN.matcher(password).matches();
    }

    public static boolean containsSqlInjectionChars(String password) {
        return password != null && SQL_INJECTION_PATTERN.matcher(password).find();
    }

}