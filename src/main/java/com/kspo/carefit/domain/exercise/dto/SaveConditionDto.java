package com.kspo.carefit.domain.exercise.dto;

import com.kspo.carefit.domain.exercise.entity.ConditionCheck;
import com.kspo.carefit.domain.exercise.enums.ConditionType;

import java.time.LocalDate;

public class SaveConditionDto {

    public record Request(
            LocalDate checkDate,
            ConditionType conditionType
    ) {}

    public record Response(
            Long conditionCheckId,
            LocalDate checkDate,
            ConditionType conditionType
    ) {
        public static Response from(ConditionCheck conditionCheck) {
            return new Response(
                    conditionCheck.getId(),
                    conditionCheck.getCheckDate(),
                    conditionCheck.getConditionType()
            );
        }
    }
}
