package com.kspo.carefit.domain.exercise.dto;

import com.kspo.carefit.domain.exercise.entity.Exercise;

import java.time.LocalDateTime;

public class EndExerciseDto {

    public record Request(
            Long exerciseId,
            LocalDateTime endTime,
            String notes
    ) {}

    public record Response(
            Long exerciseId,
            String exerciseName,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Integer durationMinutes,
            String notes
    ) {
        public static Response from(Exercise exercise) {
            return new Response(
                    exercise.getId(),
                    exercise.getExerciseName(),
                    exercise.getStartTime(),
                    exercise.getEndTime(),
                    exercise.getDurationMinutes(),
                    exercise.getNotes()
            );
        }
    }
}
