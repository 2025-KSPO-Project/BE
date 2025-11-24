package com.kspo.carefit.base.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kspo.carefit.base.config.app.OpenAIConfig;
import com.kspo.carefit.domain.exercise.exception.ExerciseException;
import com.kspo.carefit.domain.exercise.exception.ExerciseExceptionEnum;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAIClient {

    private final OpenAiService openAiService;
    private final OpenAIConfig openAIConfig;
    private final ObjectMapper objectMapper;

    /**
     * OpenAI API를 호출하여 운동 추천을 받습니다.
     *
     * @param prompt 사용자 정보 및 요청사항이 담긴 프롬프트
     * @return LLM 응답 (JSON 형식)
     */
    public Map<String, String> getExerciseRecommendation(String prompt) {
        try {
            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                    .model(openAIConfig.getModel())
                    .messages(List.of(
                            new ChatMessage(ChatMessageRole.SYSTEM.value(),
                                    "당신은 전문 운동 코치입니다. 사용자의 컨디션과 운동 이력을 고려하여 적절한 운동을 추천해주세요. 반드시 JSON 형식으로 응답해야 합니다."),
                            new ChatMessage(ChatMessageRole.USER.value(), prompt)
                    ))
                    .temperature(0.7)
                    .maxTokens(500)
                    .build();

            ChatCompletionResult result = openAiService.createChatCompletion(chatCompletionRequest);

            String response = result.getChoices().get(0).getMessage().getContent();
            log.info("OpenAI API Response: {}", response);

            return parseResponse(response);

        } catch (Exception e) {
            log.error("OpenAI API 호출 중 오류 발생: {}", e.getMessage(), e);
            throw new ExerciseException(ExerciseExceptionEnum.LLM_API_ERROR);
        }
    }

    /**
     * LLM 응답을 파싱하여 Map으로 변환합니다.
     */
    private Map<String, String> parseResponse(String response) {
        try {
            // JSON 형식의 응답을 파싱
            String jsonContent = extractJsonFromResponse(response);
            return objectMapper.readValue(jsonContent, Map.class);
        } catch (JsonProcessingException e) {
            log.error("LLM 응답 파싱 중 오류 발생: {}", e.getMessage(), e);
            throw new ExerciseException(ExerciseExceptionEnum.LLM_API_ERROR);
        }
    }

    /**
     * 응답에서 JSON 부분만 추출합니다.
     */
    private String extractJsonFromResponse(String response) {
        // 마크다운 코드 블록 제거
        String cleaned = response.trim();
        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7);
        } else if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3);
        }
        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.length() - 3);
        }
        return cleaned.trim();
    }
}
