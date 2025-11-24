package com.kspo.carefit.domain.exercise.controller;

import com.kspo.carefit.base.config.exception.dto.ApiResult;
import com.kspo.carefit.domain.exercise.dto.*;
import com.kspo.carefit.domain.exercise.facade.ExerciseFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/exercise")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseFacade exerciseFacade;

    /**
     * 컨디션 체크 저장
     */
    @PostMapping("/condition")
    public ResponseEntity<ApiResult<SaveConditionDto.Response>> saveCondition(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody SaveConditionDto.Request request) {

        SaveConditionDto.Response response = exerciseFacade.saveCondition(
                userDetails.getUsername(),
                request
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.success(response));
    }

    /**
     * 운동 시작
     */
    @PostMapping("/start")
    public ResponseEntity<ApiResult<StartExerciseDto.Response>> startExercise(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody StartExerciseDto.Request request) {

        StartExerciseDto.Response response = exerciseFacade.startExercise(
                userDetails.getUsername(),
                request
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.success(response));
    }

    /**
     * 운동 종료
     */
    @PostMapping("/end")
    public ResponseEntity<ApiResult<EndExerciseDto.Response>> endExercise(
            @RequestBody EndExerciseDto.Request request) {

        EndExerciseDto.Response response = exerciseFacade.endExercise(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.success(response));
    }

    /**
     * 현재 진행 중인 운동 조회
     */
    @GetMapping("/current")
    public ResponseEntity<ApiResult<GetCurrentExerciseDto.Response>> getCurrentExercise(
            @AuthenticationPrincipal UserDetails userDetails) {

        Optional<GetCurrentExerciseDto.Response> response = exerciseFacade.getCurrentExercise(
                userDetails.getUsername()
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.success(response.orElse(null)));
    }

    /**
     * 운동 일정 생성
     */
    @PostMapping("/schedule")
    public ResponseEntity<ApiResult<CreateScheduleDto.Response>> createSchedule(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CreateScheduleDto.Request request) {

        CreateScheduleDto.Response response = exerciseFacade.createSchedule(
                userDetails.getUsername(),
                request
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.success(response));
    }

    /**
     * 날짜별 일정 조회
     */
    @GetMapping("/schedule")
    public ResponseEntity<ApiResult<List<GetScheduleDto.Response>>> getSchedulesByDate(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam LocalDate date) {

        List<GetScheduleDto.Response> response = exerciseFacade.getSchedulesByDate(
                userDetails.getUsername(),
                date
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.success(response));
    }

    /**
     * 일정 수정
     */
    @PatchMapping("/schedule/{scheduleId}")
    public ResponseEntity<ApiResult<UpdateScheduleDto.Response>> updateSchedule(
            @PathVariable Long scheduleId,
            @RequestBody UpdateScheduleDto.Request request) {

        UpdateScheduleDto.Response response = exerciseFacade.updateSchedule(scheduleId, request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.success(response));
    }

    /**
     * 일정 삭제
     */
    @DeleteMapping("/schedule/{scheduleId}")
    public ResponseEntity<ApiResult<Void>> deleteSchedule(@PathVariable Long scheduleId) {

        exerciseFacade.deleteSchedule(scheduleId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.successNoContent());
    }

    /**
     * 이번달 운동 통계 조회
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResult<GetStatsDto.Response>> getMonthlyStats(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam int year,
            @RequestParam int month) {

        GetStatsDto.Response response = exerciseFacade.getMonthlyStats(
                userDetails.getUsername(),
                year,
                month
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.success(response));
    }

    /**
     * 캘린더 형식 조회
     */
    @GetMapping("/calendar")
    public ResponseEntity<ApiResult<GetCalendarDto.Response>> getCalendar(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam int year,
            @RequestParam int month) {

        GetCalendarDto.Response response = exerciseFacade.getCalendar(
                userDetails.getUsername(),
                year,
                month
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.success(response));
    }

    /**
     * 특정 날짜 운동 상세 조회
     */
    @GetMapping("/detail")
    public ResponseEntity<ApiResult<List<GetDetailDto.Response>>> getExerciseDetail(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam LocalDate date) {

        List<GetDetailDto.Response> response = exerciseFacade.getExerciseDetail(
                userDetails.getUsername(),
                date
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.success(response));
    }

    /**
     * 운동 추천받기
     */
    @PostMapping("/recommend")
    public ResponseEntity<ApiResult<RecommendExerciseDto.Response>> recommendExercise(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody RecommendExerciseDto.Request request) {

        RecommendExerciseDto.Response response = exerciseFacade.recommendExercise(
                userDetails.getUsername(),
                request
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.success(response));
    }
}
