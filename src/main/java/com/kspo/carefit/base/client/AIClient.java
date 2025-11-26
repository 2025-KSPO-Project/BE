package com.kspo.carefit.base.client;

import com.kspo.carefit.domain.exercise.enums.GuideType;

import java.util.Map;

public interface AIClient {

    /**
     * AI API를 호출하여 운동 추천을 받습니다.
     *
     * @param prompt 사용자 정보 및 요청사항이 담긴 프롬프트
     * @return AI 응답 (JSON 형식)
     */
    Map<String, String> getExerciseRecommendation(String prompt);

    /**
     * AI API를 호출하여 운동 가이드를 받습니다.
     *
     * @param prompt 사용자 정보 및 요청사항이 담긴 프롬프트
     * @param guideType 가이드 유형 (PRE, DURING, POST)
     * @return AI 응답 (JSON 형식)
     */
    Map<String, String> getExerciseGuide(String prompt, GuideType guideType);

    /**
     * 사용 중인 AI 제공자 이름을 반환합니다.
     *
     * @return AI 제공자 이름 (예: "OpenAI", "Gemini")
     */
    String getProviderName();
}
