package com.kspo.carefit.domain.exercise.dto;

import com.kspo.carefit.domain.exercise.entity.ExerciseSchedule;

import java.time.LocalDate;
import java.time.LocalTime;

public class CreateScheduleDto {

    public record Request(
            String exerciseName,
            LocalDate scheduledDate,
            LocalTime scheduledTime,
            Integer durationMinutes,
            String notes
    ) {}

    public record Response(
            Long scheduleId,
            String exerciseName,
            LocalDate scheduledDate,
            LocalTime scheduledTime,
            Integer durationMinutes
    ) {
        public static Response from(ExerciseSchedule schedule) {
            return new Response(
                    schedule.getId(),
                    schedule.getExerciseName(),
                    schedule.getScheduledDate(),
                    schedule.getScheduledTime(),
                    schedule.getDurationMinutes()
            );
        }
    }
}
