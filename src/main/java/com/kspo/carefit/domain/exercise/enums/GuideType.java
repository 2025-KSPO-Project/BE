package com.kspo.carefit.domain.exercise.enums;

import com.kspo.carefit.base.config.constants.enums.CodeCommInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GuideType implements CodeCommInterface {

    PRE("PRE", "운동 전 가이드"),
    DURING("DURING", "운동 중 가이드"),
    POST("POST", "운동 후 가이드");

    private final String code;
    private final String codeName;
}
