package com.kspo.carefit.damain.user.controller;

import com.kspo.carefit.base.config.exception.BaseExceptionEnum;
import com.kspo.carefit.base.config.exception.dto.ApiResult;
import com.kspo.carefit.base.security.oauth2.facade.UserOauth2facade;
import com.kspo.carefit.damain.user.dto.request.UserUpdateCodesRequest;
import com.kspo.carefit.damain.user.dto.response.UserUpdateCodesResponse;
import com.kspo.carefit.damain.user.dto.response.UserProfileResponse;
import com.kspo.carefit.damain.user.dto.response.UserSignOutResponse;
import com.kspo.carefit.damain.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;
    private final UserOauth2facade userOauth2facade;

    // 회원의 프로필을 가져오는 API ( 차후 시군구,시도,장애코드 테이블 완성 후 수정필요 )
    @GetMapping("/profile")
    public ResponseEntity<ApiResult<UserProfileResponse>> getProfile
            (@AuthenticationPrincipal UserDetails userDetails){

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult
                        .success(userFacade
                                .getUserProfile(userDetails
                                        .getUsername())));

    }

    // 회원의 장애코드 , 시도 , 시군구 코드를 설정하는 API
    @PostMapping("/update/codes")
    public ResponseEntity<ApiResult<UserUpdateCodesResponse>> addCodes
            (@AuthenticationPrincipal UserDetails userDetails,
             @RequestBody UserUpdateCodesRequest request){

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult
                        .success(userFacade.updateCodes(request)));

    }

    // 회원탈퇴 로직
    @PostMapping("/signout")
    public ResponseEntity<ApiResult<UserSignOutResponse>> signOut
            (@AuthenticationPrincipal UserDetails userDetails){

        boolean success = userOauth2facade.revokeToken(userFacade
                .getUserIdByUsername(userDetails
                        .getUsername()));

        if(success) {
            // true 를 반환한 경우
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResult
                            .success(userFacade
                                    .signOut(userDetails
                                            .getUsername())));
        }
        else {
            // false 를 반환한 경우
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(ApiResult
                            .fail(BaseExceptionEnum
                                    .BAD_REQUEST));
        }

    }


}
