package com.kspo.carefit.base.security.auth.controller;

import com.kspo.carefit.base.config.exception.dto.ApiResult;
import com.kspo.carefit.base.security.auth.dto.response.ReissueResponse;
import com.kspo.carefit.base.security.auth.facade.ReissueFacade;
import com.kspo.carefit.base.security.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final ReissueFacade reissueFacade;
    private final CookieUtil cookieUtil;

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
