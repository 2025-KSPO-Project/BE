package com.kspo.carefit.domain.exercise.dto;

import com.kspo.carefit.domain.exercise.entity.Exercise;
import com.kspo.carefit.domain.exercise.enums.ConditionType;

import java.time.LocalDateTime;

public class StartExerciseDto {

    public record Request(
            String exerciseName,
            LocalDateTime startTime,
            ConditionType conditionStatus
    ) {}

    public record Response(
            Long exerciseId,
            String exerciseName,
            LocalDateTime startTime,
            ConditionType conditionStatus
    ) {
        public static Response from(Exercise exercise) {
            return new Response(
                    exercise.getId(),
                    exercise.getExerciseName(),
                    exercise.getStartTime(),
                    exercise.getConditionStatus()
            );
        }
    }
}
