package com.kspo.carefit.base.security.handler;

import com.kspo.carefit.base.security.Oauth2UserImpl;
import com.kspo.carefit.base.security.oauth2.entity.UserOauth2Token;
import com.kspo.carefit.base.security.oauth2.facade.UserOauth2facade;
import com.kspo.carefit.base.security.util.CookieUtil;
import com.kspo.carefit.base.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain chain,
                                        Authentication authentication)
            throws IOException, ServletException {

        Oauth2UserImpl oAuth2User = (Oauth2UserImpl) authentication.getPrincipal();

        String role = getRole(oAuth2User);
        String username = oAuth2User.getUsername();

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        OAuth2AuthorizedClient client = oAuth2AuthorizedClientService.loadAuthorizedClient(
                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(),
                oAuth2AuthenticationToken.getName()
        );

        UserOauth2Token tokenEntity = userOauth2facade
                .findTokenEntityByUsername(oAuth2User
                        .getUsername());

        updateTokens(tokenEntity,client); // access 토큰 외 정보를 DB에 저장

        // 서비스 자체 토큰 발급
        String serviceAccessToken = jwtUtil.createJwt("access",username,role,24*60*60*1000L);
        String serviceRefreshToken = jwtUtil.createJwt("refresh",username,role,14*24*60*60*1000L);

        log.info("Service access Token : {}",serviceAccessToken);
        log.info("Service refresh Token : {}",serviceRefreshToken);

        response.setHeader("Authorization",serviceAccessToken);
        response.addCookie(cookieUtil.createCookie("refresh",serviceRefreshToken));


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

    // OAuth2AuthorizedClient 로 부터 refresh 토큰 및 파기기한을 추출한 뒤 , DB에 업데이트 하는 메소드
    private void updateTokens(UserOauth2Token tokenEntity,
                             OAuth2AuthorizedClient client){

        String socialRefreshToken = Objects.requireNonNull(client
                        .getRefreshToken())
                .getTokenValue();

        Instant refreshExpiration = client
                .getRefreshToken()
                .getExpiresAt();

        Instant accessExpiration = client
                .getAccessToken()
                .getExpiresAt();

        tokenEntity.updateTokens(socialRefreshToken,
                refreshExpiration,
                accessExpiration);

    }
}
