package com.kspo.carefit.damain.user.facade;

import com.kspo.carefit.base.security.oauth2.entity.UserOauth2Token;
import com.kspo.carefit.base.security.oauth2.service.UserOauth2TokenService;
import com.kspo.carefit.damain.user.dto.request.UserUpdateCodesRequest;
import com.kspo.carefit.damain.user.dto.response.UserUpdateCodesResponse;
import com.kspo.carefit.damain.user.dto.response.UserProfileResponse;
import com.kspo.carefit.damain.user.dto.response.UserSignOutResponse;
import com.kspo.carefit.damain.user.entity.User;
import com.kspo.carefit.damain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;
    private final UserOauth2TokenService userOauth2TokenService;


    // User 와 OAuth2Token 객체를 DB에 저장하는 메소드
    public void saveUserWithOauth2Token(String accessToken,
                                        String username,
                                        String email,
                                        String name){

        User user = userService.checkExistingUser(username,email,name); // User 존재 여부 파악후 , user 객체 가져오기

        Optional<UserOauth2Token> existingToken = userOauth2TokenService.
                findByUserIdWithOptional(user.getId());

        if (existingToken.isEmpty()) { // 토큰이 존재하지 않을 경우 , 토큰 객체 생성하기

            UserOauth2Token tokenEntity = userOauth2TokenService
                    .addSocialAccessToken(accessToken);

            user.addUserOauth2Token(tokenEntity);

            userService.saveUser(user);

        } else { // 기존의 토큰이 존재할 경우 , 토큰 업데이트

            UserOauth2Token tokenEntity = existingToken.get();
            userOauth2TokenService.updateAccessToken(accessToken,tokenEntity);
        }


    }

    // 회원정보를 가져오는 메소드
    public UserProfileResponse getUserProfile(String username){

        User userEntity = userService.findByUsername(username);

        return new UserProfileResponse(userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getDisabilityCode());

    }

    // 회원 탈퇴 메소드
    @Transactional
    public UserSignOutResponse signOut(String username){

            User userEntity = userService.findByUsername(username);
            userService.deleteUser(username);

            return new UserSignOutResponse(userEntity.getUsername(),"해당 유저의 정보가 삭제되었습니다.");

    }

    // User 객체를 username 을 통해 가져오는 메소드
    public Long getUserIdByUsername(String username){
        return userService.findByUsername(username).getId();
    }

    // 회원의 장애정보 , 시도 , 시군구 코드를 설정하는 메소드
    @Transactional
    public UserUpdateCodesResponse updateCodes(String username,UserUpdateCodesRequest request){

        User userEntity = userService.updateCodes(username,
                request.disabilityCode(),
                request.sidoCode(),
                request.sigunguCode());

        userService.saveUser(userEntity);

        return new UserUpdateCodesResponse(userEntity.getDisabilityCode(),
                        userEntity.getSidoCode(),
                        userEntity.getSigunguCode());

    }

}
