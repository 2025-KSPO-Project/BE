package com.kspo.carefit.base.security.oauth2.dto.response;

public record NaverRefreshResponse(String access_token,
                                   String token_type,
                                   String expires_in) {
}
