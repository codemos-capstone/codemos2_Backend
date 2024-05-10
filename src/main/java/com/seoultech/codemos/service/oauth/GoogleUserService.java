package com.seoultech.codemos.service.oauth;


import com.seoultech.codemos.dto.TokenDto;
import com.seoultech.codemos.jwt.TokenProvider;
import com.seoultech.codemos.model.Authority;
import com.seoultech.codemos.model.UserEntity;
import com.seoultech.codemos.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleUserService {

    private static UserRepository userRepository;
    @Autowired
    public GoogleUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public void registerOrUpdateGoogleUser(String email, String name) {

        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseGet(UserEntity::new);
        userEntity.setEmail(email);
        userEntity.setNickname(name);
        Authority authorities = Authority.ROLE_USER;
        userEntity.setAuthority(authorities);

    }
    public static boolean isNewUser(String email){
        return !userRepository.existsByEmail(email);
    }
}
