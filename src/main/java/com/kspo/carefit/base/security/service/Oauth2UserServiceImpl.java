package com.kspo.carefit.base.security.service;

import com.kspo.carefit.base.security.Oauth2UserImpl;
import com.kspo.carefit.base.security.oauth2.dto.request.CustomOAuth2UserRequest;
import com.kspo.carefit.base.security.oauth2.dto.response.NaverResponse;
import com.kspo.carefit.base.security.oauth2.dto.response.OAuth2Response;
import com.kspo.carefit.damain.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class Oauth2UserServiceImpl extends DefaultOAuth2UserService {

    private final UserFacade userFacade;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2Response oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        String username = oAuth2Response.getProvider()
                + " "
                + oAuth2Response.getProviderId();

        OAuth2AccessToken socialAccessToken = userRequest.getAccessToken();

        log.info("로그인 한 유저 - {}",oAuth2Response.getName());
        log.info("네이버 제공 access Token : {}",socialAccessToken.getTokenValue());

        userFacade.saveUserWithOauth2Token(socialAccessToken.getTokenValue(),
                username,
                oAuth2Response.getEmail(),
                oAuth2Response.getName());

        return new Oauth2UserImpl(
                new CustomOAuth2UserRequest("ROLE_USER", // Role
                        oAuth2Response.getName(), // Name
                        username)); // Username

    }
}
