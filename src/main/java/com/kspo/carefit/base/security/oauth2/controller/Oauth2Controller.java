package com.kspo.carefit.base.security.oauth2.controller;

import com.kspo.carefit.base.config.exception.dto.ApiResult;
import com.kspo.carefit.base.security.oauth2.dto.response.NaverRefreshResponse;
import com.kspo.carefit.base.security.oauth2.facade.UserOauth2facade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "OAuth2", description = "OAuth2 인증 관리 API")
@RequestMapping("/custom/oauth2")
@RequiredArgsConstructor
@RestController
public class Oauth2Controller {

    private final UserOauth2facade userOauth2facade;

    @Operation(summary = "OAuth2 토큰 갱신", description = "네이버 OAuth2 액세스 토큰을 갱신합니다.")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResult<NaverRefreshResponse>> refreshToken
            (@Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResult
                        .success(userOauth2facade
                                .refreshToken(userDetails
                                        .getUsername())));

    }


}
