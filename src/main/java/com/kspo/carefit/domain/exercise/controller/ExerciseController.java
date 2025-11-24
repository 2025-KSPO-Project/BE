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
}
