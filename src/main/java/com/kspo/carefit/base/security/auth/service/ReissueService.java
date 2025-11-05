package com.kspo.carefit.base.security.auth.service;

import com.kspo.carefit.base.config.exception.BaseExceptionEnum;
import com.kspo.carefit.base.config.exception.domain.BaseException;
import com.kspo.carefit.base.security.auth.dto.response.ReissueResponse;
import com.kspo.carefit.base.security.auth.entity.Refresh;
import com.kspo.carefit.base.security.auth.repository.RefreshRepository;
import com.kspo.carefit.base.security.util.CookieUtil;
import com.kspo.carefit.base.security.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReissueService {

    private final RefreshRepository refreshRepository;
    private final JwtUtil jwtUtil;

    // access , refresh 토큰을 갱신하는 메소드
    public ReissueResponse reissue(String refresh){

        try {

            jwtUtil.isExpired(refresh); // refresh 가 파기되었을 경우

        } catch ( ExpiredJwtException e ){

            log.info("Refresh token had expired : {} ", Instant.now().toString());
            throw new BaseException(BaseExceptionEnum.REFRESH_TOKEN_EXPIRED);

        }

        return new ReissueResponse(reissueAccess(refresh), // access 갱신
                reissueRefresh(refresh)); // refresh 갱신

    }

    // refresh 토큰을 갱신하는 메소드
    public String reissueRefresh(String refresh){

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        String newRefresh = jwtUtil.createJwt("refresh",username,role,14*24*60*60*1000L);
        saveRefresh(newRefresh); // DB에 refresh 저장

        return newRefresh;

    }

    // access 토큰을 갱신하는 메소드
    public String reissueAccess(String refresh){

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        return jwtUtil.createJwt("access",username,role,24*60*60*1000L);

    }


    /*
    기본 CRUD 메소드 모음
     */

    // refresh 토큰을 DB에 저장하는 메소드
    public void saveRefresh(String refresh){

        Refresh refreshEntity = Refresh.builder()
                .refreshToken(refresh)
                .username(jwtUtil.getUsername(refresh))
                .expiration(Instant.now().plusSeconds(14 * 24 * 60 * 60))
                .build();

        refreshRepository.save(refreshEntity);

    }

    // refresh 토큰을 DB에서 삭제하는 메소드
    public void deleteRefresh(String refresh){

        refreshRepository.deleteByRefreshToken(refresh);

    }

}
