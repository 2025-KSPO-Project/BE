package com.kspo.carefit.base.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kspo.carefit.base.config.app.GeminiConfig;
import com.kspo.carefit.domain.exercise.exception.ExerciseException;
import com.kspo.carefit.domain.exercise.exception.ExerciseExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("geminiClient")
@RequiredArgsConstructor
@ConditionalOnExpression("!'${spring.gemini.api-key:}'.isEmpty() && '${spring.openai.api-key:}'.isEmpty()")
public class GeminiClient implements AIClient {

    private final GeminiConfig geminiConfig;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public GeminiClient(GeminiConfig geminiConfig, ObjectMapper objectMapper) {
        this.geminiConfig = geminiConfig;
        this.objectMapper = objectMapper;
        this.restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(geminiConfig.getTimeout()))
                .setReadTimeout(Duration.ofMillis(geminiConfig.getTimeout()))
                .build();
    }

    @Override
    public Map<String, String> getExerciseRecommendation(String prompt) {
        try {
            String url = String.format("%s/%s:generateContent?key=%s",
                    geminiConfig.getApiUrl(),
                    geminiConfig.getModel(),
                    geminiConfig.getApiKey());

            Map<String, Object> requestBody = buildRequestBody(prompt);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return parseResponse(response.getBody());
            } else {
                log.error("Gemini API 호출 실패: {}", response.getStatusCode());
                throw new ExerciseException(ExerciseExceptionEnum.LLM_API_ERROR);
            }

        } catch (Exception e) {
            log.error("Gemini API 호출 중 오류 발생: {}", e.getMessage(), e);
            throw new ExerciseException(ExerciseExceptionEnum.LLM_API_ERROR);
        }
    }

    private Map<String, Object> buildRequestBody(String prompt) {
        Map<String, Object> requestBody = new HashMap<>();

        Map<String, Object> systemInstruction = new HashMap<>();
        systemInstruction.put("role", "user");
        systemInstruction.put("parts", List.of(
                Map.of("text", "당신은 전문 운동 코치입니다. 사용자의 컨디션과 운동 이력을 고려하여 적절한 운동을 추천해주세요. 반드시 JSON 형식으로 응답해야 합니다.")
        ));

        Map<String, Object> content = new HashMap<>();
        content.put("role", "user");
        content.put("parts", List.of(
                Map.of("text", prompt)
        ));

        requestBody.put("system_instruction", systemInstruction);
        requestBody.put("contents", List.of(content));

        Map<String, Object> generationConfig = new HashMap<>();
        generationConfig.put("temperature", 0.7);
        generationConfig.put("maxOutputTokens", 500);
        requestBody.put("generationConfig", generationConfig);

        return requestBody;
    }

    private Map<String, String> parseResponse(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode candidates = root.path("candidates");

            if (candidates.isArray() && !candidates.isEmpty()) {
                JsonNode firstCandidate = candidates.get(0);
                JsonNode content = firstCandidate.path("content");
                JsonNode parts = content.path("parts");

                if (parts.isArray() && !parts.isEmpty()) {
                    String text = parts.get(0).path("text").asText();
                    log.info("Gemini API Response: {}", text);

                    String jsonContent = extractJsonFromResponse(text);
                    return objectMapper.readValue(jsonContent, Map.class);
                }
            }

            log.error("Gemini API 응답 파싱 실패: {}", responseBody);
            throw new ExerciseException(ExerciseExceptionEnum.LLM_API_ERROR);

        } catch (JsonProcessingException e) {
            log.error("Gemini 응답 파싱 중 오류 발생: {}", e.getMessage(), e);
            throw new ExerciseException(ExerciseExceptionEnum.LLM_API_ERROR);
        }
    }

    private String extractJsonFromResponse(String response) {
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

    @Override
    public String getProviderName() {
        return "Gemini";
    }
}
