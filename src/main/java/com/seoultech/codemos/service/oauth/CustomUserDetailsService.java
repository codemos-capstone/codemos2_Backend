package com.seoultech.codemos.service.oauth;

import com.seoultech.codemos.model.UserEntity;
import com.seoultech.codemos.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            return createUserDetails(user);
        } else {
            throw new UsernameNotFoundException(email + " 을 DB에서 찾을 수 없습니다.");
        }
    }

    private UserDetails createUserDetails(UserEntity userEntity) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userEntity.getAuthority().toString());
        return new User(
                String.valueOf(userEntity.getEmail()),
                userEntity.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}