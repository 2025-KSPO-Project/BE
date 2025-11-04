package com.kspo.carefit.base.security.oauth2.dto.request;

public record CustomOAuth2UserRequest(String role,
                                     String name,
                                     String username) {
}
