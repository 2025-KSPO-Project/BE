package com.kspo.carefit.base.security;

import com.kspo.carefit.base.security.oauth2.dto.request.CustomOAuth2UserRequest;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
@Builder
public class Oauth2UserImpl implements OAuth2User {

    private final CustomOAuth2UserRequest customOAuth2UserRequest;


    @Override
    public Map<String, Object> getAttributes() {

        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return customOAuth2UserRequest.role();
            }
        });

        return collection;
    }

    @Override
    public String getName() {
        return customOAuth2UserRequest.name();
    }

    public String getUsername(){
        return customOAuth2UserRequest.username();
    }

}
