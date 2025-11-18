package com.kspo.carefit.base.security.oauth2.dto.response;

import java.util.Map;

public record NaverResponse(Map<String,Object> attribute) implements OAuth2Response {

    @SuppressWarnings("unchecked")
    public NaverResponse(Map<String,Object> attribute){

        this.attribute = (Map<String, Object>) attribute.get("response");

    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return attribute
                .get("id")
                .toString();
    }

    @Override
    public String getEmail() {
        return attribute
                .get("email")
                .toString();
    }

    @Override
    public String getName() {
        return attribute
                .get("name")
                .toString();
    }
}
