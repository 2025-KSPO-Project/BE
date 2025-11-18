package com.kspo.carefit.base.security.filter;

import com.kspo.carefit.base.security.UserDetailsImpl;
import com.kspo.carefit.base.security.util.CookieUtil;
import com.kspo.carefit.base.security.util.JwtUtil;
import com.kspo.carefit.damain.user.service.UserService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    // 메인 필터 메소드
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = extractToken(request.getHeader("Authorization"));

        // 요청에 대한 토큰 및 URI 검사
        if (checkUri(request.getRequestURI())) { // 토큰 파기 , null , 카테고리 검사

            filterChain.doFilter(request, response);
            return;
        }

        if(checkToken(authorization)){

            filterChain.doFilter(request,response);
            return;
        }

        SecurityContextHolder
                .getContext()
                .setAuthentication(makeAuthentication(authorization));

        filterChain.doFilter(request,response);
    }


    /*
    내부 유틸 메소드
     */

    // 로그인 , OAuth2 관련 URI 여부를 체크하는 메소드
    private boolean checkUri(String uri){

        return uri.matches("^\\/login(?:\\/.*)?$") ||
                uri.matches("^\\/oauth2(?:\\/.*)?$");
    }

    // 토큰 이상 여부를 체크하는 메소드
    private boolean checkToken(String token){

        return (token == null) || // 토큰이 빈 경우
                (jwtUtil.isExpired(token)) || // 토큰이 파기된 경우
                !(jwtUtil.getCategory(token).equals("access")); // 토큰의 카테고리가 맞지 않은경우
    }

    // Authentication 등록을 위한 시큐리티 컨텍스트 객체생성 메소드
    private Authentication makeAuthentication(String token){

        String username = jwtUtil.getUsername(token);
        UserDetailsImpl userDetails = new UserDetailsImpl(userService.findByUsername(username));

        return new UsernamePasswordAuthenticationToken
                (userDetails,
                        null,
                        userDetails.getAuthorities());

    }

    // 헤더에서 Bearer를 추출하는 메소드
    private String extractToken(String token){
        if(token == null || !token.startsWith("Bearer ")){
            return null;
        }
        return token.substring(7);
    }
}
