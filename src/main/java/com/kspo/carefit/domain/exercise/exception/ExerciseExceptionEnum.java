package com.kspo.carefit.domain.exercise.exception;

import com.kspo.carefit.base.enums.MessageCommInterface;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ExerciseExceptionEnum implements MessageCommInterface {

    // Exercise 관련 예외
    EXERCISE_NOT_FOUND("EXERCISE.EXCEPTION.NOT_FOUND", "운동 기록을 찾을 수 없습니다."),
    EXERCISE_ALREADY_STARTED("EXERCISE.EXCEPTION.ALREADY_STARTED", "이미 진행 중인 운동이 있습니다."),
    EXERCISE_NOT_STARTED("EXERCISE.EXCEPTION.NOT_STARTED", "시작되지 않은 운동입니다."),
    EXERCISE_ALREADY_ENDED("EXERCISE.EXCEPTION.ALREADY_ENDED", "이미 종료된 운동입니다."),

    // Schedule 관련 예외
    SCHEDULE_NOT_FOUND("SCHEDULE.EXCEPTION.NOT_FOUND", "운동일정을 찾을 수 없습니다."),
    SCHEDULE_ALREADY_COMPLETED("SCHEDULE.EXCEPTION.ALREADY_COMPLETED", "이미 완료된 일정입니다."),

    // ConditionCheck 관련 예외
    CONDITION_CHECK_NOT_FOUND("CONDITION_CHECK.EXCEPTION.NOT_FOUND", "컨디션 체크 기록을 찾을 수 없습니다."),

    // Recommendation 관련 예외
    RECOMMENDATION_NOT_FOUND("RECOMMENDATION.EXCEPTION.NOT_FOUND", "운동 추천 기록을 찾을 수 없습니다."),

    // LLM 관련 예외
    LLM_API_ERROR("LLM.EXCEPTION.API_ERROR", "운동 추천 중 오류가 발생했습니다."),
    LLM_TIMEOUT("LLM.EXCEPTION.TIMEOUT", "운동 추천 시간이 초과되었습니다.")
    ;

    private final String errorCode;
    private final String message;

    @Override
    public String getCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
