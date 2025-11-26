package com.kspo.carefit.base.security.oauth2.dto.response;

public interface OAuth2Response {

    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();

}
