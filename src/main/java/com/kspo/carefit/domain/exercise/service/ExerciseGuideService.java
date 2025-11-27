package com.kspo.carefit.domain.exercise.service;

import com.kspo.carefit.base.client.AIClient;
import com.kspo.carefit.base.client.AIClientFactory;
import com.kspo.carefit.damain.user.entity.User;
import com.kspo.carefit.domain.exercise.dto.ExerciseGuideDto;
import com.kspo.carefit.domain.exercise.enums.ConditionType;
import com.kspo.carefit.domain.exercise.enums.GuideType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExerciseGuideService {

    private final AIClientFactory aiClientFactory;

    /**
     * 운동 가이드 생성
     */
    public ExerciseGuideDto.Response getExerciseGuide(User user, ExerciseGuideDto.Request request) {
        // LLM 프롬프트 생성
        String prompt = buildGuidePrompt(user, request);

        // AI 클라이언트를 통해 가이드 받기
        AIClient aiClient = aiClientFactory.getAIClient();
        Map<String, String> llmResult = aiClient.getExerciseGuide(prompt, request.guideType());

        return ExerciseGuideDto.Response.of(request.guideType(), llmResult);
    }

    /**
     * LLM 프롬프트 생성
     */
    private String buildGuidePrompt(User user, ExerciseGuideDto.Request request) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("사용자 정보:\n");
        prompt.append("- 장애유형 코드: ").append(user.getDisabilityCode()).append("\n");
        prompt.append("- 장애유형명: ").append(getDisabilityName(user.getDisabilityCode())).append("\n");
        prompt.append("- 운동 종목: ").append(request.sportName()).append("\n");

        if (request.conditionType() != null) {
            prompt.append("- 오늘 컨디션: ").append(request.conditionType().getCodeName()).append("\n");
        }

        if (request.additionalNotes() != null && !request.additionalNotes().isBlank()) {
            prompt.append("- 추가 특이사항: ").append(request.additionalNotes()).append("\n");
        }

        prompt.append("\n").append(getGuideTypeInstruction(request.guideType()));
        prompt.append("\n\nJSON 형식으로 응답해주세요:\n");
        prompt.append(getJsonFormat(request.guideType()));

        return prompt.toString();
    }

    /**
     * 장애유형 코드를 이름으로 변환
     */
    private String getDisabilityName(Integer disabilityCode) {
        if (disabilityCode == null) {
            return "정보 없음";
        }
        // TODO: 실제로는 코드 테이블에서 조회해야 함
        return switch (disabilityCode) {
            case 1 -> "지체장애";
            case 2 -> "뇌병변장애";
            case 3 -> "시각장애";
            case 4 -> "청각장애";
            case 5 -> "발달장애(지적/자폐)";
            default -> "기타장애";
        };
    }

    /**
     * 가이드 타입별 지시사항
     */
    private String getGuideTypeInstruction(GuideType guideType) {
        return switch (guideType) {
            case PRE -> """
                위 정보를 바탕으로 운동 전 준비사항을 안내해주세요.
                - 장애유형에 맞는 준비운동과 스트레칭
                - 장비 및 환경 점검 사항
                - 컨디션에 따른 주의사항
                - 체크해야 할 항목들
                """;
            case DURING -> """
                위 정보를 바탕으로 운동 중 주의사항을 안내해주세요.
                - 장애유형에 맞는 적절한 운동 강도
                - 휴식 타이밍과 수분 섭취
                - 자세 교정 포인트
                - 위험 신호와 즉시 중단해야 할 상황
                """;
            case POST -> """
                위 정보를 바탕으로 운동 후 회복 가이드를 안내해주세요.
                - 쿨다운 운동과 스트레칭
                - 수분 및 영양 섭취 권장사항
                - 피로 회복을 위한 휴식 방법
                - 다음 운동 전까지 주의사항
                """;
        };
    }

    /**
     * JSON 응답 형식
     */
    private String getJsonFormat(GuideType guideType) {
        String title = switch (guideType) {
            case PRE -> "운동 전 준비사항";
            case DURING -> "운동 중 주의사항";
            case POST -> "운동 후 회복 가이드";
        };

        return """
            {
              "title": "%s",
              "content": "상세한 가이드 내용을 작성해주세요",
              "checklist": "체크항목1, 체크항목2, 체크항목3",
              "caution": "특별히 주의해야 할 사항",
              "estimated_minutes": "예상 소요 시간(분)"
            }
            """.formatted(title);
    }
}
