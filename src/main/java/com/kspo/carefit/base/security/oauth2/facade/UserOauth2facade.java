package com.kspo.carefit.base.security.oauth2.facade;

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
}
