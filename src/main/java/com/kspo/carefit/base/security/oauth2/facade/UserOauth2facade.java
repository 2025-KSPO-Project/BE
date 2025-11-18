package com.kspo.carefit.base.security.oauth2.facade;

import com.kspo.carefit.base.security.oauth2.dto.response.NaverRefreshResponse;
import com.kspo.carefit.base.security.oauth2.entity.UserOauth2Token;
import com.kspo.carefit.base.security.oauth2.service.UserOauth2TokenService;
import com.kspo.carefit.damain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
public class UserOauth2facade {

    private final UserService userService;
    private final UserOauth2TokenService userOauth2TokenService;

    public UserOauth2Token findTokenEntityByUsername(String username){

        return userOauth2TokenService
                .findByUserId(userService
                        .findByUsername(username)
                        .getId());

    }

    public boolean revokeToken(Long id){

        return userOauth2TokenService.revokeToken(id);

    }

    public void addRefreshToken(UserOauth2Token tokenEntity,String refresh){

        userOauth2TokenService
                .addRefreshToken(tokenEntity,refresh);

    }

    public void updateTokenEntity(UserOauth2Token tokenEntity){

        userOauth2TokenService.updateTokenEntity(tokenEntity);

    }

    public NaverRefreshResponse refreshToken(String username){

        UserOauth2Token tokenEntity = findTokenEntityByUsername(username);

        NaverRefreshResponse response = userOauth2TokenService // Client 를 통한 API 호출 결과를 가져오기
                .refreshToken(tokenEntity.getRefreshToken(),
                        tokenEntity.getUser().getId());

        userOauth2TokenService
                .updateAccessToken(response.access_token(),tokenEntity); // API 호출 결과를 토큰 객체에 반영하기

        updateTokenEntity(tokenEntity);

        return response;
    }
}
