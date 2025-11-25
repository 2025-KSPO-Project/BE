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
    REFRESH_TOKEN_EXPIRED("BASE.EXCEPTION.REFRESH_TOKEN_EXPIRED","Refresh 토큰이 파기되었거나 존재하지 않습니다. 재로그인이 필요합니다."),
    ACCESS_TOKEN_EXPIRED("BASE.EXCEPTION.ACCESS_TOKEN_EXPIRED","Access 토큰이 파기되었거나 존재하지 않습니다. 재로그인이 필요합니다."),
    LOGOUT_FAILED("BASE.EXCEPTION.LOGOUT_FAILED","로그아웃에 실패하였습니다."),
    UNSUPPORTED_PROVIDER("BASE.EXCEPTION.UNSUPPORTED_PROVIDER","해당 소셜 로그인은 지원하지 않습니다."),
    CARPOOL_NOT_FOUND("BASE.EXCEPTION.CARPOOL_NOT_FOUND","해당 사용자의 카풀 글을 찾을 수 없습니다."),
    WRITER_UNMATCHED("BASE.EXCEPTION.WRITER_UNMATCHED","사용자와 글쓴이가 일치하지 않습니다."),
    APPLY_NOT_FOUND("BASE.EXCEPTION.APPLY_NOT_FOUND","사용자의 해당 카풀에 대한 지원을 찾을 수 없습니다."),
    CLUBS_NOT_FOUND("BASE.EXCEPTION.CLUBS_NOT_FOUND","해당 스포츠 클럽을 찾을 수 없습니다."),
    COMPETITION_NOT_FOUND("BASE.EXCEPTION.COMPETITION_NOT_FOUND","해당 대회정보를 찾을 수 없습니다."),
    FACILITY_NOT_FOUND("BASE.EXCEPTION.FACILITY_NOT_FOUND","해당 시설정보를 찾을 수 없습니다.")
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
