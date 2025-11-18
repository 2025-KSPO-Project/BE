package com.kspo.carefit.base.security.oauth2.controller;

import com.kspo.carefit.base.config.exception.dto.ApiResult;
import com.kspo.carefit.base.security.oauth2.dto.response.NaverRefreshResponse;
import com.kspo.carefit.base.security.oauth2.facade.UserOauth2facade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/custom/oauth2")
@RequiredArgsConstructor
@RestController
public class Oauth2Controller {

    private final UserOauth2facade userOauth2facade;

    @PostMapping("/refresh")
    public ResponseEntity<ApiResult<NaverRefreshResponse>> refreshToken
            (@AuthenticationPrincipal UserDetails userDetails){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResult
                        .success(userOauth2facade
                                .refreshToken(userDetails
                                        .getUsername())));

    }


}
