package com.seoultech.codemos.repository;


import com.seoultech.codemos.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<UserEntity> findById(Long id);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByNickname(String nickname);
}
