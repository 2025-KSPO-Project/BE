package com.kspo.carefit.domain.exercise.facade;

import com.kspo.carefit.damain.user.entity.User;
import com.kspo.carefit.damain.user.service.UserService;
import com.kspo.carefit.domain.exercise.dto.*;
import com.kspo.carefit.domain.exercise.entity.ConditionCheck;
import com.kspo.carefit.domain.exercise.entity.Exercise;
import com.kspo.carefit.domain.exercise.entity.ExerciseRecommendation;
import com.kspo.carefit.domain.exercise.entity.ExerciseSchedule;
import com.kspo.carefit.domain.exercise.service.ConditionCheckService;
import com.kspo.carefit.domain.exercise.service.ExerciseGuideService;
import com.kspo.carefit.domain.exercise.service.ExerciseRecommendationService;
import com.kspo.carefit.domain.exercise.service.ExerciseScheduleService;
import com.kspo.carefit.domain.exercise.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ExerciseFacade {

    private final ExerciseService exerciseService;
    private final ConditionCheckService conditionCheckService;
    private final ExerciseScheduleService exerciseScheduleService;
    private final ExerciseRecommendationService exerciseRecommendationService;
    private final ExerciseGuideService exerciseGuideService;
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

    /**
     * 이번달 운동 통계 조회
     */
    @Transactional(readOnly = true)
    public GetStatsDto.Response getMonthlyStats(String username, int year, int month) {
        User user = userService.findByUsername(username);

        // 해당 월의 시작일과 마지막일 계산
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = YearMonth.of(year, month).atEndOfMonth();

        // 기간별 운동 조회
        List<Exercise> exercises = exerciseService.getExercisesByDateRange(user.getId(), startDate, endDate);

        // 종료된 운동만 필터링
        List<Exercise> completedExercises = exercises.stream()
                .filter(e -> e.getEndTime() != null)
                .collect(Collectors.toList());

        // 통계 계산
        int totalExerciseDays = (int) completedExercises.stream()
                .map(Exercise::getExerciseDate)
                .distinct()
                .count();

        int totalDurationMinutes = completedExercises.stream()
                .mapToInt(e -> e.getDurationMinutes() != null ? e.getDurationMinutes() : 0)
                .sum();

        int averageDurationMinutes = totalExerciseDays > 0 ? totalDurationMinutes / totalExerciseDays : 0;

        // 가장 많이 한 운동
        String mostFrequentExercise = completedExercises.stream()
                .collect(Collectors.groupingBy(Exercise::getExerciseName, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        // 날짜별 운동 목록
        List<GetStatsDto.ExerciseByDate> exerciseByDate = completedExercises.stream()
                .map(e -> new GetStatsDto.ExerciseByDate(
                        e.getExerciseDate(),
                        e.getExerciseName(),
                        e.getDurationMinutes()
                ))
                .collect(Collectors.toList());

        return new GetStatsDto.Response(
                totalExerciseDays,
                totalDurationMinutes,
                averageDurationMinutes,
                mostFrequentExercise,
                exerciseByDate
        );
    }

    /**
     * 캘린더 형식 조회
     */
    @Transactional(readOnly = true)
    public GetCalendarDto.Response getCalendar(String username, int year, int month) {
        User user = userService.findByUsername(username);

        // 해당 월의 시작일과 마지막일 계산
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = YearMonth.of(year, month).atEndOfMonth();

        // 기간별 운동 조회
        List<Exercise> exercises = exerciseService.getExercisesByDateRange(user.getId(), startDate, endDate);

        // 날짜별로 그룹화
        Map<LocalDate, List<Exercise>> exercisesByDate = exercises.stream()
                .filter(e -> e.getEndTime() != null)
                .collect(Collectors.groupingBy(Exercise::getExerciseDate));

        // 모든 날짜에 대해 ExerciseDay 생성
        List<GetCalendarDto.ExerciseDay> exerciseDays = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            List<Exercise> dayExercises = exercisesByDate.getOrDefault(date, Collections.emptyList());
            boolean hasExercise = !dayExercises.isEmpty();
            int exerciseCount = dayExercises.size();
            int totalDuration = dayExercises.stream()
                    .mapToInt(e -> e.getDurationMinutes() != null ? e.getDurationMinutes() : 0)
                    .sum();

            exerciseDays.add(new GetCalendarDto.ExerciseDay(
                    date,
                    hasExercise,
                    exerciseCount,
                    totalDuration
            ));
        }

        return new GetCalendarDto.Response(year, month, exerciseDays);
    }

    /**
     * 특정 날짜 운동 상세 조회
     */
    @Transactional(readOnly = true)
    public List<GetDetailDto.Response> getExerciseDetail(String username, LocalDate date) {
        User user = userService.findByUsername(username);

        return exerciseService.getExercisesByDate(user.getId(), date).stream()
                .map(GetDetailDto.Response::from)
                .collect(Collectors.toList());
    }

    /**
     * 운동 추천받기
     */
    @Transactional
    public RecommendExerciseDto.Response recommendExercise(String username, RecommendExerciseDto.Request request) {
        User user = userService.findByUsername(username);

        // 최근 3일간 운동 기록 조회
        LocalDate today = LocalDate.now();
        LocalDate threeDaysAgo = today.minusDays(3);
        List<Exercise> recentExercises = exerciseService.getExercisesByDateRange(
                user.getId(),
                threeDaysAgo,
                today
        );

        // 추천 생성
        ExerciseRecommendation recommendation = exerciseRecommendationService.createRecommendation(
                user,
                request.conditionType(),
                recentExercises
        );

        // 추천 이유 추출 (llmResponse에서)
        String reason = recommendation.getLlmResponse();

        return RecommendExerciseDto.Response.from(recommendation, reason);
    }

    /**
     * 운동 가이드 조회 (운동 전/중/후)
     */
    @Transactional(readOnly = true)
    public ExerciseGuideDto.Response getExerciseGuide(String username, ExerciseGuideDto.Request request) {
        User user = userService.findByUsername(username);
        return exerciseGuideService.getExerciseGuide(user, request);
    }
}
