package com.kspo.carefit.damain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Sido {

    SEOUL("서울",1);

    private final String name;
    private final Integer code;

}
