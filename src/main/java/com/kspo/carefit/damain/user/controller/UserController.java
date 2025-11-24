package com.kspo.carefit.damain.user.controller;

import com.kspo.carefit.base.config.exception.BaseExceptionEnum;
import com.kspo.carefit.base.config.exception.dto.ApiResult;
import com.kspo.carefit.base.security.oauth2.facade.UserOauth2facade;
import com.kspo.carefit.base.security.util.CookieUtil;
import com.kspo.carefit.damain.user.dto.request.UserUpdateCodesRequest;
import com.kspo.carefit.damain.user.dto.response.UserExistsResponse;
import com.kspo.carefit.damain.user.dto.response.UserUpdateCodesResponse;
import com.kspo.carefit.damain.user.dto.response.UserProfileResponse;
import com.kspo.carefit.damain.user.dto.response.UserSignOutResponse;
import com.kspo.carefit.damain.user.facade.UserFacade;
import jakarta.servlet.http.HttpServletResponse;
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
    private final CookieUtil cookieUtil;

    @PostMapping("/check-exists")
    public ResponseEntity<ApiResult<UserExistsResponse>> checkExists
            (@AuthenticationPrincipal UserDetails userDetails){

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult
                        .success(userFacade
                                .checkExists(userDetails
                                        .getUsername())));

    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResult<UserProfileResponse>> getProfile
            (@AuthenticationPrincipal UserDetails userDetails){

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult
                        .success(userFacade
                                .getUserProfile(userDetails
                                        .getUsername())));

    }

    @PatchMapping("/update/codes")
    public ResponseEntity<ApiResult<UserUpdateCodesResponse>> addCodes
            (@AuthenticationPrincipal UserDetails userDetails,
             @RequestBody UserUpdateCodesRequest request){

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult
                        .success(userFacade.updateCodes(userDetails.getUsername(),request)));

    }

    @PostMapping("/signout")
    public ResponseEntity<ApiResult<UserSignOutResponse>> signOut
            (@AuthenticationPrincipal UserDetails userDetails,
             HttpServletResponse response){

        boolean success = userOauth2facade.revokeToken(userFacade
                .getUserIdByUsername(userDetails
                        .getUsername()));

        if(success) {
            cookieUtil.zeroCookie(response);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResult
                            .success(userFacade
                                    .signOut(userDetails
                                            .getUsername())));
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(ApiResult
                            .fail(BaseExceptionEnum
                                    .BAD_REQUEST));
        }

    }


}
