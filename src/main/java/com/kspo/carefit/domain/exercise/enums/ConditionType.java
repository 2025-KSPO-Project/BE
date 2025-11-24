package com.kspo.carefit.domain.exercise.enums;

import com.kspo.carefit.base.config.constants.enums.CodeCommInterface;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConditionType implements CodeCommInterface {

    EXERCISED_YESTERDAY("EXERCISED_YESTERDAY", "전날 운동을 했음"),
    INJURED("INJURED", "부상이 있음"),
    REHABILITATION("REHABILITATION", "현재 재활중"),
    NONE("NONE", "해당사항 없음");

    private final String code;
    private final String codeName;
}
