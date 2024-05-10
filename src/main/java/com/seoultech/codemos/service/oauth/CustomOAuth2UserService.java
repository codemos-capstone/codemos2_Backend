package com.seoultech.codemos.service.oauth;

import com.seoultech.codemos.dto.TokenDto;
import com.seoultech.codemos.jwt.TokenProvider;
import com.seoultech.codemos.model.UserEntity;
import com.seoultech.codemos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private GoogleUserService googleUserService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        if (userRequest == null) {
            throw new IllegalArgumentException("OAuth2UserRequest cannot be null");
        }
        System.out.println("NEWUSER!!!");
        OAuth2User oAuth2User;
        try {
            oAuth2User = super.loadUser(userRequest);
            if (oAuth2User == null) {
                throw new OAuth2AuthenticationException("Failed to load user from OAuth2 provider");
            }
            return processOAuth2User(userRequest, oAuth2User);
        } catch (OAuth2AuthenticationException e) {
            // More detailed error logging
            System.out.println("OAuth2AuthenticationException: " + e.toString());
            throw e;
        } catch (Exception ex) {
            // More detailed error logging
            System.out.println("Exception occurred: " + ex.toString());
            throw new OAuth2AuthenticationException(String.valueOf(ex));
        }
    }


    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        System.out.println("UserEmail: " + email);
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);
        UserEntity user;
        if (userOptional.isEmpty()) {
            googleUserService.registerOrUpdateGoogleUser(email, name);
            return oAuth2User;
        }

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        OAuth2AuthenticationToken authToken = new OAuth2AuthenticationToken(oAuth2User, authorities, userRequest.getClientRegistration().getRegistrationId());

        TokenDto tokenDto = tokenProvider.generateTokenDto(authToken);
        System.out.println("Generated JWT: " + tokenDto.getAccessToken());

        return oAuth2User;
    }

}
