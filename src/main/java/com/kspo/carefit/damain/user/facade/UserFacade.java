package com.kspo.carefit.damain.user.facade;

import com.kspo.carefit.base.security.oauth2.entity.UserOauth2Token;
import com.kspo.carefit.base.security.oauth2.service.UserOauth2TokenService;
import com.kspo.carefit.damain.user.entity.User;
import com.kspo.carefit.damain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;
    private final UserOauth2TokenService userOauth2TokenService;

    @Transactional
    public void saveUserWithOauth2Token(String accessToken,
                                        String username,
                                        String email,
                                        String name){

        UserOauth2Token tokenEntity = userOauth2TokenService
                .addSocialAccessToken(accessToken);

        User user = userService.checkExistingUser(username,email,name);
        user.addUserOauth2Token(tokenEntity);

        userService.saveUser(user);

    }


}
