package com.seoultech.codemos.config;

import com.seoultech.codemos.dto.TokenDto;
import com.seoultech.codemos.jwt.TokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@Component
public class SecurityConfig {

    private final TokenProvider tokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable); //csrf비활성화
        http.cors(Customizer.withDefaults());

        //HTTP 기본 인증 비활성화, 폼로그인 비활성화
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers("/home/home").permitAll()
                        .requestMatchers("/login/**").permitAll()
                        .anyRequest().authenticated()
        );
        http.oauth2Login(oauth2 -> oauth2
                .successHandler((request, response, authentication) -> {
                    if (authentication instanceof OAuth2AuthenticationToken) {
                        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
                        TokenDto tokenDto = tokenProvider.generateTokenDto(oauthToken);

                        response.setContentType("application/json;charset=UTF-8");
                        response.setStatus(HttpServletResponse.SC_OK);
                        PrintWriter writer = response.getWriter();
                        writer.println("{\"access_token\": \"" + tokenDto.getAccessToken() + "\", " +
                                "\"token_type\": \"" + tokenDto.getGrantType() + "\", " +
                                "\"expires_in\": " + tokenDto.getTokenExpiresIn() + "}");
                        writer.flush();
                        writer.close();
                    } else {
                        response.sendRedirect("/home/loginFailure"); // 이 경우는 일반적으로 발생하지 않지만 안전을 위해 처리
                    }
                })
                .failureHandler((request, response, exception) -> {
                    response.sendRedirect("/home/loginFailure"); // 실패 시 이동할 경로
                })
        );

        return http.build();
    }
}
