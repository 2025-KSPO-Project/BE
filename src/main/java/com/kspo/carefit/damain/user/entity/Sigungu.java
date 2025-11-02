package com.kspo.carefit.damain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Sigungu {

    // 예시 코드
    GANGNAMGU("강남구",11111);

    private final String name;
    private final Integer code;

}
