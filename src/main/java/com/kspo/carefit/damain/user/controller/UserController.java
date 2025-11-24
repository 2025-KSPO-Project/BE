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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "사용자 관리 API")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;
    private final UserOauth2facade userOauth2facade;
    private final CookieUtil cookieUtil;

    @Operation(summary = "회원 등록 여부 확인", description = "사용자가 이미 등록된 회원인지 확인합니다.")
    @PostMapping("/check-exists")
    public ResponseEntity<ApiResult<UserExistsResponse>> checkExists
            (@Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails){

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult
                        .success(userFacade
                                .checkExists(userDetails
                                        .getUsername())));

    }

    @Operation(summary = "회원 프로필 조회", description = "사용자의 프로필 정보를 조회합니다.")
    @GetMapping("/profile")
    public ResponseEntity<ApiResult<UserProfileResponse>> getProfile
            (@Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails){

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult
                        .success(userFacade
                                .getUserProfile(userDetails
                                        .getUsername())));

    }

    @Operation(summary = "회원 코드 정보 수정", description = "사용자의 장애코드, 시도, 시군구 코드를 설정합니다.")
    @PatchMapping("/update/codes")
    public ResponseEntity<ApiResult<UserUpdateCodesResponse>> addCodes
            (@Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
             @RequestBody UserUpdateCodesRequest request){

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult
                        .success(userFacade.updateCodes(userDetails.getUsername(),request)));

    }

    @Operation(summary = "회원 탈퇴", description = "사용자 계정을 탈퇴합니다.")
    @PostMapping("/signout")
    public ResponseEntity<ApiResult<UserSignOutResponse>> signOut
            (@Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
             @Parameter(hidden = true) HttpServletResponse response){

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
