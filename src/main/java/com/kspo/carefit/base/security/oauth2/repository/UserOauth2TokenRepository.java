package com.kspo.carefit.base.security.oauth2.repository;

import com.kspo.carefit.base.security.oauth2.entity.UserOauth2Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserOauth2TokenRepository extends JpaRepository<UserOauth2Token,Long> {

    Optional<UserOauth2Token> findByAccessToken(String accessToken);

    @Query("SELECT t FROM UserOauth2Token t WHERE t.user.id = :userId")
    Optional<UserOauth2Token> findTokenByUserId(@Param("userId") Long userId);

}
