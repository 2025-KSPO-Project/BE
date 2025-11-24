package com.kspo.carefit.domain.exercise.dto;

import com.kspo.carefit.domain.exercise.entity.ExerciseRecommendation;
import com.kspo.carefit.domain.exercise.enums.ConditionType;

public class RecommendExerciseDto {

    public record Request(
            ConditionType conditionType
    ) {}

    public record Response(
            Long recommendationId,
            String exerciseName,
            String sportName,
            String reason
    ) {
        public static Response from(ExerciseRecommendation recommendation, String reason) {
            return new Response(
                    recommendation.getId(),
                    recommendation.getExerciseName(),
                    recommendation.getSportName(),
                    reason
            );
        }
    }
}
