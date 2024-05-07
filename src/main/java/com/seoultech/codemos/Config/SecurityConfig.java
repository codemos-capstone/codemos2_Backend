package com.seoultech.codemos.Config;

import com.seoultech.codemos.dto.TokenDto;
import com.seoultech.codemos.jwt.TokenProvider;
import com.seoultech.codemos.service.oauth.CustomOAuth2UserService;
import com.seoultech.codemos.service.oauth.CustomUserDetailsService;
import com.seoultech.codemos.service.oauth.OAuth2AuthenticationSuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;



@Configuration
@EnableWebSecurity
@Component
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Autowired
    public SecurityConfig(TokenProvider tokenProvider, CustomUserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }


    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;





    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable); //csrf비활성화
        http.cors(Customizer.withDefaults());

        //HTTP 기본 인증 비활성화, 폼로그인 비활성화
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers("/home/home").permitAll()
                        .requestMatchers("/login/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/auth/sign").permitAll()
                        .requestMatchers("/auth/login").permitAll()
                        .anyRequest().authenticated()
        );

        http.oauth2Login(oauth2 -> oauth2
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler((request, response, exception) -> {
                    response.sendRedirect("/google/loginFailure"); // 실패 시 이동할 경로
                })
        );

        return http.build();
    }

}
