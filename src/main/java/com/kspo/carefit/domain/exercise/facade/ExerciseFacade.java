package com.kspo.carefit.domain.exercise.facade;

import com.kspo.carefit.damain.user.entity.User;
import com.kspo.carefit.damain.user.service.UserService;
import com.kspo.carefit.domain.exercise.dto.*;
import com.kspo.carefit.domain.exercise.entity.ConditionCheck;
import com.kspo.carefit.domain.exercise.entity.Exercise;
import com.kspo.carefit.domain.exercise.entity.ExerciseSchedule;
import com.kspo.carefit.domain.exercise.service.ConditionCheckService;
import com.kspo.carefit.domain.exercise.service.ExerciseScheduleService;
import com.kspo.carefit.domain.exercise.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ExerciseFacade {

    private final ExerciseService exerciseService;
    private final ConditionCheckService conditionCheckService;
    private final ExerciseScheduleService exerciseScheduleService;
    private final UserService userService;

    /**
     * 컨디션 체크 저장
     */
    @Transactional
    public SaveConditionDto.Response saveCondition(String username, SaveConditionDto.Request request) {
        User user = userService.findByUsername(username);

        ConditionCheck conditionCheck = conditionCheckService.saveCondition(
                user,
                request.checkDate(),
                request.conditionType()
        );

        return SaveConditionDto.Response.from(conditionCheck);
    }

    /**
     * 운동 시작
     */
    @Transactional
    public StartExerciseDto.Response startExercise(String username, StartExerciseDto.Request request) {
        User user = userService.findByUsername(username);

        Exercise exercise = exerciseService.startExercise(
                user,
                request.exerciseName(),
                request.startTime(),
                request.conditionStatus()
        );

        return StartExerciseDto.Response.from(exercise);
    }

    /**
     * 운동 종료
     */
    @Transactional
    public EndExerciseDto.Response endExercise(EndExerciseDto.Request request) {
        Exercise exercise = exerciseService.endExercise(
                request.exerciseId(),
                request.endTime(),
                request.notes()
        );

        return EndExerciseDto.Response.from(exercise);
    }

    /**
     * 현재 진행 중인 운동 조회
     */
    @Transactional(readOnly = true)
    public Optional<GetCurrentExerciseDto.Response> getCurrentExercise(String username) {
        User user = userService.findByUsername(username);

        return exerciseService.getCurrentExercise(user.getId())
                .map(GetCurrentExerciseDto.Response::from);
    }

    /**
     * 운동 일정 생성
     */
    @Transactional
    public CreateScheduleDto.Response createSchedule(String username, CreateScheduleDto.Request request) {
        User user = userService.findByUsername(username);

        ExerciseSchedule schedule = exerciseScheduleService.createSchedule(
                user,
                request.exerciseName(),
                request.scheduledDate(),
                request.scheduledTime(),
                request.durationMinutes(),
                request.notes()
        );

        return CreateScheduleDto.Response.from(schedule);
    }

    /**
     * 날짜별 일정 조회
     */
    @Transactional(readOnly = true)
    public List<GetScheduleDto.Response> getSchedulesByDate(String username, LocalDate date) {
        User user = userService.findByUsername(username);

        return exerciseScheduleService.getSchedulesByDate(user.getId(), date)
                .stream()
                .map(GetScheduleDto.Response::from)
                .collect(Collectors.toList());
    }

    /**
     * 일정 수정
     */
    @Transactional
    public UpdateScheduleDto.Response updateSchedule(Long scheduleId, UpdateScheduleDto.Request request) {
        ExerciseSchedule schedule = exerciseScheduleService.updateSchedule(
                scheduleId,
                request.exerciseName(),
                request.scheduledDate(),
                request.scheduledTime(),
                request.durationMinutes(),
                request.notes()
        );

        return UpdateScheduleDto.Response.from(schedule);
    }

    /**
     * 일정 삭제
     */
    @Transactional
    public void deleteSchedule(Long scheduleId) {
        exerciseScheduleService.deleteSchedule(scheduleId);
    }
}
