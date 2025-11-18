package com.kspo.carefit.damain.user.dto.response;

public record UserExistsResponse(boolean exists,
                                 String message) {
}
