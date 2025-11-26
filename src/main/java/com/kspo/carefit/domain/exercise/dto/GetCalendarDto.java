package com.kspo.carefit.domain.exercise.dto;

import java.time.LocalDate;
import java.util.List;

public class GetCalendarDto {

    public record Response(
            Integer year,
            Integer month,
            List<ExerciseDay> exercises
    ) {}

    public record ExerciseDay(
            LocalDate date,
            Boolean hasExercise,
            Integer exerciseCount,
            Integer totalDuration
    ) {}
}
