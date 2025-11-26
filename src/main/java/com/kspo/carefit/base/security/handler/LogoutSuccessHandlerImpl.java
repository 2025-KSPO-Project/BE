package com.kspo.carefit.base.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kspo.carefit.base.config.exception.BaseExceptionEnum;
import com.kspo.carefit.base.config.exception.domain.BaseException;
import com.kspo.carefit.base.config.exception.dto.ApiResult;
import com.kspo.carefit.base.security.auth.facade.ReissueFacade;
import com.kspo.carefit.base.security.util.CookieUtil;
import com.kspo.carefit.base.security.util.JwtUtil;
import com.kspo.carefit.damain.user.dto.response.UserLogoutResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;
    private final ReissueFacade reissueFacade;
    private final ObjectMapper objectMapper;

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication)
            throws IOException, ServletException {

        try {

            String refresh = cookieUtil.findRefreshFromCookie(request);
            String username = jwtUtil.getUsername(refresh);
            if (username == null){ // 토큰 값이 비어있을 경우

                throw new BaseException(BaseExceptionEnum.EXCEPTION_VALIDATION);

            }

            // DB 에서 refresh 토큰 삭제
            reissueFacade.deleteRefresh(refresh);

           // 쿠키 비우기
            cookieUtil.zeroCookie(response);

            // 성공 응답
            ApiResult<UserLogoutResponse> result = ApiResult
                    .success(new UserLogoutResponse(username," 해당 회원이 로그아웃하였습니다."));

            response.setStatus(HttpStatus.OK.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            objectMapper.writeValue(response.getWriter(), result);

        } catch (Exception e) {
            // 로그아웃 실패 시
            throw new BaseException(BaseExceptionEnum.LOGOUT_FAILED);
        }
    }
}

