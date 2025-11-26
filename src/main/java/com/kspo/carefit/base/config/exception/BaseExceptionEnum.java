package com.kspo.carefit.base.config.exception;

import com.kspo.carefit.base.enums.MessageCommInterface;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BaseExceptionEnum implements MessageCommInterface {
    EXCEPTION_ISSUED("BASE.EXCEPTION.EXCEPTION_ISSUED", "시스템에러 발생, 관리자에게 문의하세요."),
    EXCEPTION_VALIDATION("BASE.EXCEPTION.EXCEPTION_VALIDATION", "요청 값 검증 실패"),
    ENTITY_NOT_FOUND("BASE.EXCEPTION.ENTITY_NOT_FOUND", "조회 대상이 없습니다."),
    FORBIDDEN("BASE.EXCEPTION.FORBIDDEN", "인증 실패 : {0}"),
    BAD_REQUEST("BASE.EXCEPTION.BAD_REQUEST", "잘못된 요청입니다."),
    ACCESS_DENIED("BASE.EXCEPTION.ACCESS_DENIED", "{0}"),
    ;

    private final String errorCode;
    private final String message;

    @Override
    public String getCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
