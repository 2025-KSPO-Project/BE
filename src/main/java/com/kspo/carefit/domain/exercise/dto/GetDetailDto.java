package com.kspo.carefit.domain.exercise.dto;

import com.kspo.carefit.domain.exercise.entity.Exercise;
import com.kspo.carefit.domain.exercise.enums.ConditionType;

import java.time.LocalDateTime;

public class GetDetailDto {

    public record Response(
            Long exerciseId,
            String exerciseName,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Integer durationMinutes,
            ConditionType conditionStatus,
            String notes
    ) {
        public static Response from(Exercise exercise) {
            return new Response(
                    exercise.getId(),
                    exercise.getExerciseName(),
                    exercise.getStartTime(),
                    exercise.getEndTime(),
                    exercise.getDurationMinutes(),
                    exercise.getConditionStatus(),
                    exercise.getNotes()
            );
        }
    }
}
