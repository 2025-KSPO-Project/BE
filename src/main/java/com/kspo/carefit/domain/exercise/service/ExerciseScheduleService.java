package com.kspo.carefit.domain.exercise.service;

import com.kspo.carefit.base.config.exception.domain.BaseException;
import com.kspo.carefit.damain.user.entity.User;
import com.kspo.carefit.domain.exercise.entity.ExerciseSchedule;
import com.kspo.carefit.domain.exercise.exception.ExerciseExceptionEnum;
import com.kspo.carefit.domain.exercise.repository.ExerciseScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ExerciseScheduleService {

    private final ExerciseScheduleRepository exerciseScheduleRepository;

    /**
     * 운동 일정 생성
     */
    public ExerciseSchedule createSchedule(User user, String exerciseName, LocalDate scheduledDate,
                                          LocalTime scheduledTime, Integer durationMinutes, String notes) {
        ExerciseSchedule schedule = ExerciseSchedule.builder()
                .user(user)
                .exerciseName(exerciseName)
                .scheduledDate(scheduledDate)
                .scheduledTime(scheduledTime)
                .durationMinutes(durationMinutes)
                .notes(notes)
                .isCompleted(false)
                .build();

        return exerciseScheduleRepository.save(schedule);
    }

    /**
     * 날짜별 일정 조회
     */
    @Transactional(readOnly = true)
    public List<ExerciseSchedule> getSchedulesByDate(Long userId, LocalDate date) {
        return exerciseScheduleRepository.findByUserIdAndScheduledDate(userId, date);
    }

    /**
     * 일정 수정
     */
    public ExerciseSchedule updateSchedule(Long scheduleId, String exerciseName, LocalDate scheduledDate,
                                          LocalTime scheduledTime, Integer durationMinutes, String notes) {
        ExerciseSchedule schedule = exerciseScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BaseException(ExerciseExceptionEnum.SCHEDULE_NOT_FOUND));

        schedule.updateSchedule(exerciseName, scheduledDate, scheduledTime, durationMinutes, notes);
        return exerciseScheduleRepository.save(schedule);
    }

    /**
     * 일정 삭제
     */
    public void deleteSchedule(Long scheduleId) {
        ExerciseSchedule schedule = exerciseScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BaseException(ExerciseExceptionEnum.SCHEDULE_NOT_FOUND));

        exerciseScheduleRepository.delete(schedule);
    }
}
