package com.kspo.carefit.base.security.oauth2.service;

import com.kspo.carefit.base.client.OAuth2Client;
import com.kspo.carefit.base.config.exception.BaseExceptionEnum;
import com.kspo.carefit.base.config.exception.domain.BaseException;
import com.kspo.carefit.base.security.oauth2.dto.response.NaverRefreshResponse;
import com.kspo.carefit.base.security.oauth2.entity.UserOauth2Token;
import com.kspo.carefit.base.security.oauth2.facade.UserOauth2facade;
import com.kspo.carefit.base.security.oauth2.repository.UserOauth2TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserOauth2TokenService {

    private final UserOauth2TokenRepository userOauth2TokenRepository;
    private final OAuth2Client oAuth2Client;

    @Value("${spring.jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    @Value("${spring.jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    // 토큰을 조회한 후 , 업데이트 후 UserOauth2Token 객체를 리턴하는 매소드
    public UserOauth2Token addSocialAccessToken(String accessToken){

        return userOauth2TokenRepository.findByAccessToken(accessToken) // 토큰이 존재하면 바로 반환
                .orElseGet(() -> UserOauth2Token.builder() // 토큰이 존재하지 않을 경우 생성
                        .provider("naver")
                        .accessToken(accessToken)
                        .accessTokenExpiresAt(Instant.now().plusMillis(accessTokenExpiration))
                        .build()
                );
    }

    // 소셜 로그인 토큰을 파기하는 메소드
    public boolean revokeToken(Long id){

        UserOauth2Token userOauth2Token = findByUserId(id);
        return oAuth2Client.revokeToken(userOauth2Token.getAccessToken());
    }

    public NaverRefreshResponse refreshToken(String refreshToken,Long id){

        return oAuth2Client.refreshToken(refreshToken);

    }

    /*
    기본 CRUD 메소드 모음
     */

    public void updateAccessToken(String accessToken,UserOauth2Token tokenEntity){

        tokenEntity.updateAccessToken(accessToken,Instant.now().plusMillis(accessTokenExpiration));

    }

    public void addRefreshToken(UserOauth2Token tokenEntity,
                                String refreshToken){

        tokenEntity.updateRefreshToken(refreshToken,Instant.now().plusMillis(refreshTokenExpiration));

    }

    public void updateTokenEntity(UserOauth2Token tokenEntity){

        userOauth2TokenRepository.save(tokenEntity);

    }


    // user_id를 통해 토큰 객체를 찾는 메소드
    public UserOauth2Token findByUserId(Long userId){
        return userOauth2TokenRepository
                .findTokenByUserId(userId)
                .orElseThrow(()-> new BaseException(BaseExceptionEnum
                        .ENTITY_NOT_FOUND));

    }

    public Optional<UserOauth2Token> findByUserIdWithOptional(Long userId){
        return userOauth2TokenRepository.findTokenByUserId(userId);
    }


}
