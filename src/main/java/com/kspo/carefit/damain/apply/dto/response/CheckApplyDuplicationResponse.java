package com.kspo.carefit.damain.apply.dto.response;

public record CheckApplyDuplicationResponse(String nickname,
                                            boolean checkDuplication,
                                            String message) {
}
