package com.kspo.carefit.base.security.auth.controller;

import com.kspo.carefit.base.config.exception.dto.ApiResult;
import com.kspo.carefit.base.security.auth.dto.response.ReissueResponse;
import com.kspo.carefit.base.security.auth.facade.ReissueFacade;
import com.kspo.carefit.base.security.util.CookieUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "토큰 재발급", description = "토큰 재발급 API")
@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final ReissueFacade reissueFacade;
    private final CookieUtil cookieUtil;

    @Operation(summary = "토큰 재발급", description = "Refresh 토큰을 이용하여 Access 토큰을 재발급합니다.")
    @PostMapping("/reissue")
    public ResponseEntity<ApiResult<ReissueResponse>> reissue(HttpServletRequest request,
                                                   HttpServletResponse response){

        String refresh = cookieUtil.findRefreshFromCookie(request);
        ReissueResponse reissueResponse = reissueFacade.refreshTokens(refresh);

        response.setHeader("Authorization",reissueResponse.access()); // access 토큰 추가
        response.addCookie(cookieUtil  // Cookie 에 refresh 토큰 추가
                .createCookie("refresh",reissueResponse.refresh()));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResult.success(reissueResponse));

    }
}
