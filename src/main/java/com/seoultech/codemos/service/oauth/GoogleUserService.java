package com.seoultech.codemos.service.oauth;


import com.seoultech.codemos.dto.TokenDto;
import com.seoultech.codemos.jwt.TokenProvider;
import com.seoultech.codemos.model.UserEntity;
import com.seoultech.codemos.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class GoogleUserService {

    private final UserRepository userRepository;


    @Autowired
    public GoogleUserService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    public UserEntity registerOrUpdateGoogleUser(String email, String name) {
        System.out.println("registerOrUpdateGoogleUser");
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseGet(UserEntity::new);
        userEntity.setEmail(email);
        userEntity.setNickname(name);
        SimpleGrantedAuthority authorities =new SimpleGrantedAuthority("ROLE_USER");
        userEntity.setAuthority(authorities);

        return userRepository.save(userEntity);
    }
}
