package com.capcredit.authuser.repository;


import com.capcredit.authuser.model.RefreshToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


public interface RefreshTokenRepository extends CrudRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByToken(String token);

    @Transactional
    @Modifying
    void deleteByUserId(UUID userId);
}

