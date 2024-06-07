package com.seoultech.codemos.service.oauth;

import com.seoultech.codemos.dto.TokenDto;
import com.seoultech.codemos.jwt.TokenProvider;
import com.seoultech.codemos.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private GoogleUserService googleUserService; // 사용자 정보를 관리하는 서비스
    @Value("${codemos.feServerAddress}")
    String feServerAddress;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            String userEmail = oauthToken.getPrincipal().getAttribute("email");
            boolean isNewUser = GoogleUserService.isNewUser(userEmail);
            String targetUrl;
            if (isNewUser) {
                targetUrl = feServerAddress+"/login?oauth=true&email="+userEmail;
            } else {
                TokenDto tokenDto = tokenProvider.generateTokenDto(oauthToken);
                targetUrl = feServerAddress+"/oauthMiddle#accessToken=" + tokenDto.getAccessToken();
            }

            response.sendRedirect(targetUrl);
        }
    }

}
