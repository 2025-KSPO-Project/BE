package com.kspo.carefit.domain.exercise.controller;

import com.kspo.carefit.base.config.exception.dto.ApiResult;
import com.kspo.carefit.domain.exercise.dto.*;
import com.kspo.carefit.domain.exercise.facade.ExerciseFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Tag(name = "Exercise", description = "운동 관리 API")
@RestController
@RequestMapping("/api/v1/exercise")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseFacade exerciseFacade;

    @Operation(summary = "컨디션 체크 저장", description = "사용자의 오늘 컨디션 상태를 저장합니다.")
    @PostMapping("/condition")
    public ResponseEntity<ApiResult<SaveConditionDto.Response>> saveCondition(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody SaveConditionDto.Request request) {

        SaveConditionDto.Response response = exerciseFacade.saveCondition(
                userDetails.getUsername(),
                request
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.success(response));
    }

    @Operation(summary = "운동 시작", description = "새로운 운동을 시작합니다.")
    @PostMapping("/start")
    public ResponseEntity<ApiResult<StartExerciseDto.Response>> startExercise(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody StartExerciseDto.Request request) {

        StartExerciseDto.Response response = exerciseFacade.startExercise(
                userDetails.getUsername(),
                request
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.success(response));
    }

    @Operation(summary = "운동 종료", description = "진행 중인 운동을 종료하고 결과를 저장합니다.")
    @PostMapping("/end")
    public ResponseEntity<ApiResult<EndExerciseDto.Response>> endExercise(
            @RequestBody EndExerciseDto.Request request) {

        EndExerciseDto.Response response = exerciseFacade.endExercise(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.success(response));
    }

    @Operation(summary = "현재 진행 중인 운동 조회", description = "사용자의 현재 진행 중인 운동 정보를 조회합니다.")
    @GetMapping("/current")
    public ResponseEntity<ApiResult<GetCurrentExerciseDto.Response>> getCurrentExercise(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {

        Optional<GetCurrentExerciseDto.Response> response = exerciseFacade.getCurrentExercise(
                userDetails.getUsername()
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.success(response.orElse(null)));
    }

    @Operation(summary = "운동 일정 생성", description = "새로운 운동 일정을 생성합니다.")
    @PostMapping("/schedule")
    public ResponseEntity<ApiResult<CreateScheduleDto.Response>> createSchedule(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CreateScheduleDto.Request request) {

        CreateScheduleDto.Response response = exerciseFacade.createSchedule(
                userDetails.getUsername(),
                request
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.success(response));
    }

    @Operation(summary = "날짜별 일정 조회", description = "특정 날짜의 운동 일정을 조회합니다.")
    @GetMapping("/schedule")
    public ResponseEntity<ApiResult<List<GetScheduleDto.Response>>> getSchedulesByDate(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "조회할 날짜 (yyyy-MM-dd)", example = "2024-11-24") @RequestParam LocalDate date) {

        List<GetScheduleDto.Response> response = exerciseFacade.getSchedulesByDate(
                userDetails.getUsername(),
                date
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.success(response));
    }

    @Operation(summary = "일정 수정", description = "등록된 운동 일정을 수정합니다.")
    @PatchMapping("/schedule/{scheduleId}")
    public ResponseEntity<ApiResult<UpdateScheduleDto.Response>> updateSchedule(
            @Parameter(description = "일정 ID") @PathVariable Long scheduleId,
            @RequestBody UpdateScheduleDto.Request request) {

        UpdateScheduleDto.Response response = exerciseFacade.updateSchedule(scheduleId, request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.success(response));
    }

    @Operation(summary = "일정 삭제", description = "등록된 운동 일정을 삭제합니다.")
    @DeleteMapping("/schedule/{scheduleId}")
    public ResponseEntity<ApiResult<Void>> deleteSchedule(
            @Parameter(description = "일정 ID") @PathVariable Long scheduleId) {

        exerciseFacade.deleteSchedule(scheduleId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.successNoContent());
    }

    @Operation(summary = "이번달 운동 통계 조회", description = "특정 월의 운동 통계를 조회합니다.")
    @GetMapping("/stats")
    public ResponseEntity<ApiResult<GetStatsDto.Response>> getMonthlyStats(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "연도", example = "2024") @RequestParam int year,
            @Parameter(description = "월", example = "11") @RequestParam int month) {

        GetStatsDto.Response response = exerciseFacade.getMonthlyStats(
                userDetails.getUsername(),
                year,
                month
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.success(response));
    }

    @Operation(summary = "캘린더 형식 조회", description = "특정 월의 운동 기록을 캘린더 형식으로 조회합니다.")
    @GetMapping("/calendar")
    public ResponseEntity<ApiResult<GetCalendarDto.Response>> getCalendar(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "연도", example = "2024") @RequestParam int year,
            @Parameter(description = "월", example = "11") @RequestParam int month) {

        GetCalendarDto.Response response = exerciseFacade.getCalendar(
                userDetails.getUsername(),
                year,
                month
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.success(response));
    }

    @Operation(summary = "특정 날짜 운동 상세 조회", description = "특정 날짜에 수행한 운동의 상세 정보를 조회합니다.")
    @GetMapping("/detail")
    public ResponseEntity<ApiResult<List<GetDetailDto.Response>>> getExerciseDetail(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "조회할 날짜 (yyyy-MM-dd)", example = "2024-11-24") @RequestParam LocalDate date) {

        List<GetDetailDto.Response> response = exerciseFacade.getExerciseDetail(
                userDetails.getUsername(),
                date
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.success(response));
    }

    @Operation(summary = "운동 추천받기", description = "AI를 통해 사용자의 컨디션에 맞는 운동을 추천받습니다.")
    @PostMapping("/recommend")
    public ResponseEntity<ApiResult<RecommendExerciseDto.Response>> recommendExercise(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody RecommendExerciseDto.Request request) {

        RecommendExerciseDto.Response response = exerciseFacade.recommendExercise(
                userDetails.getUsername(),
                request
        );

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResult.success(response));
    }
}
