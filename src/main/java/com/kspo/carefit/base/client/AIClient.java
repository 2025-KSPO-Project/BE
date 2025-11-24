package com.kspo.carefit.base.client;

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
     * 사용 중인 AI 제공자 이름을 반환합니다.
     *
     * @return AI 제공자 이름 (예: "OpenAI", "Gemini")
     */
    String getProviderName();
}
