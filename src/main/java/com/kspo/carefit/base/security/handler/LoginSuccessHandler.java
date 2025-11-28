package com.kspo.carefit.base.security.handler;

import com.kspo.carefit.base.security.Oauth2UserImpl;
import com.kspo.carefit.base.security.oauth2.entity.UserOauth2Token;
import com.kspo.carefit.base.security.oauth2.facade.UserOauth2facade;
import com.kspo.carefit.base.security.util.CookieUtil;
import com.kspo.carefit.base.security.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    private final UserOauth2facade userOauth2facade;
    @Value("${redirect.url}")private String value;

    @Transactional
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        Oauth2UserImpl oAuth2User = (Oauth2UserImpl) authentication.getPrincipal();

        String role = getRole(oAuth2User);
        String username = oAuth2User.getUsername();

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        var client = oAuth2AuthorizedClientService.loadAuthorizedClient(
                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(),
                oAuth2AuthenticationToken.getName()
        );

        String socialRefresh = Objects.requireNonNull(client // refresh 토큰 추출
                .getRefreshToken())
                .getTokenValue();

        UserOauth2Token tokenEntity = userOauth2facade
                .findTokenEntityByUsername(oAuth2User
                        .getUsername());

        userOauth2facade.addRefreshToken(tokenEntity,socialRefresh); // access 토큰 외 정보필드 추가
        userOauth2facade.updateTokenEntity(tokenEntity); // 토큰객체 업데이트 하기

        // 서비스 자체 토큰 발급
        String serviceAccessToken = jwtUtil.createJwt("access",username,role,24*60*60*1000L);
        String serviceRefreshToken = jwtUtil.createJwt("refresh",username,role,14*24*60*60*1000L);

        log.info("Service access Token : {}",serviceAccessToken);
        log.info("Service refresh Token : {}",serviceRefreshToken);

        response.addCookie(cookieUtil.createCookie("Authorization",serviceAccessToken));
        response.addCookie(cookieUtil.createCookie("refresh",serviceRefreshToken));
        response.sendRedirect(value);

    }

    /*
    헬퍼 메서드 모음
     */

    // OAuth2User 구현객체에서 role 을 가져오는 메소드
    private <T extends OAuth2User> String getRole(T oAuth2User){

        Collection<? extends GrantedAuthority> authorities = oAuth2User.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority authority = iterator.next();

        return authority.getAuthority();
    }


}
