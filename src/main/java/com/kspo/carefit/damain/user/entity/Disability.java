package com.kspo.carefit.damain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Disability {

    // 예시 코드
    NON_DISABILITY("비장애인",7);

    private final String name;
    private final Integer code;
}
