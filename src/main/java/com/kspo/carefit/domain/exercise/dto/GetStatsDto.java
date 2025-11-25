package com.kspo.carefit.domain.exercise.dto;

import java.time.LocalDate;
import java.util.List;

public class GetStatsDto {

    public record Response(
            Integer totalExerciseDays,
            Integer totalDurationMinutes,
            Integer averageDurationMinutes,
            String mostFrequentExercise,
            List<ExerciseByDate> exerciseByDate
    ) {}

    public record ExerciseByDate(
            LocalDate date,
            String exerciseName,
            Integer durationMinutes
    ) {}
}
