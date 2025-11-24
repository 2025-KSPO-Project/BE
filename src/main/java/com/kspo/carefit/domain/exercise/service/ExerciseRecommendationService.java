package com.kspo.carefit.domain.exercise.service;

import com.kspo.carefit.damain.user.entity.User;
import com.kspo.carefit.domain.exercise.entity.Exercise;
import com.kspo.carefit.domain.exercise.entity.ExerciseRecommendation;
import com.kspo.carefit.domain.exercise.enums.ConditionType;
import com.kspo.carefit.domain.exercise.repository.ExerciseRecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ExerciseRecommendationService {

    private final ExerciseRecommendationRepository exerciseRecommendationRepository;

    /**
     * 운동 추천 생성 및 저장
     * TODO: 실제 LLM API 연동 필요
     */
    public ExerciseRecommendation createRecommendation(User user, ConditionType conditionType,
                                                       List<Exercise> recentExercises) {
        LocalDate today = LocalDate.now();

        // LLM 프롬프트 생성
        String llmPrompt = buildLLMPrompt(user, conditionType, recentExercises);

        // TODO: 실제 LLM API 호출
        // String llmResponse = callLLMAPI(llmPrompt);
        // LLM 응답 파싱
        // RecommendationResult result = parseLLMResponse(llmResponse);

        // 임시: Mock 데이터 사용
        String mockExerciseName = getMockExerciseName(conditionType);
        String mockSportName = getMockSportName(conditionType);
        String mockLLMResponse = getMockLLMResponse(conditionType);

        ExerciseRecommendation recommendation = ExerciseRecommendation.builder()
                .user(user)
                .recommendationDate(today)
                .exerciseName(mockExerciseName)
                .sportName(mockSportName)
                .conditionType(conditionType)
                .llmPrompt(llmPrompt)
                .llmResponse(mockLLMResponse)
                .isAccepted(false)
                .build();

        return exerciseRecommendationRepository.save(recommendation);
    }

    /**
     * LLM 프롬프트 생성
     */
    private String buildLLMPrompt(User user, ConditionType conditionType, List<Exercise> recentExercises) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("사용자 정보:\n");
        prompt.append("- 관심 스포츠 코드: ").append(user.getSportsCode()).append("\n");
        prompt.append("- 오늘 컨디션: ").append(conditionType.getCodeName()).append("\n");
        prompt.append("- 최근 운동 기록:\n");

        recentExercises.forEach(exercise -> {
            prompt.append("  - ").append(exercise.getExerciseDate())
                    .append(": ").append(exercise.getExerciseName())
                    .append(" (").append(exercise.getDurationMinutes()).append("분)\n");
        });

        prompt.append("\n위 정보를 바탕으로 오늘 할 수 있는 적절한 운동을 추천해주세요.\n");
        prompt.append("JSON 형식으로 응답해주세요:\n");
        prompt.append("{\n");
        prompt.append("  \"sport_name\": \"추천 스포츠명\",\n");
        prompt.append("  \"exercise_name\": \"추천 운동명\",\n");
        prompt.append("  \"reason\": \"추천 이유\"\n");
        prompt.append("}");

        return prompt.toString();
    }

    /**
     * Mock 데이터 생성 (실제 LLM API 연동 전까지 사용)
     */
    private String getMockExerciseName(ConditionType conditionType) {
        return switch (conditionType) {
            case EXERCISED_YESTERDAY -> "가벼운 스트레칭";
            case INJURED -> "재활 운동";
            case REHABILITATION -> "물리치료 운동";
            case NONE -> "유산소 운동";
        };
    }

    private String getMockSportName(ConditionType conditionType) {
        return switch (conditionType) {
            case EXERCISED_YESTERDAY -> "요가";
            case INJURED -> "재활";
            case REHABILITATION -> "물리치료";
            case NONE -> "러닝";
        };
    }

    private String getMockLLMResponse(ConditionType conditionType) {
        return switch (conditionType) {
            case EXERCISED_YESTERDAY -> "전날 운동을 하셨기 때문에 가벼운 스트레칭을 추천드립니다.";
            case INJURED -> "부상이 있으시니 재활 운동을 천천히 진행하시는 것을 추천드립니다.";
            case REHABILITATION -> "재활 중이시니 물리치료 운동을 지속하시는 것이 좋겠습니다.";
            case NONE -> "컨디션이 좋으시니 유산소 운동을 추천드립니다.";
        };
    }

    /**
     * 추천 수락
     */
    public void acceptRecommendation(Long recommendationId) {
        ExerciseRecommendation recommendation = exerciseRecommendationRepository.findById(recommendationId)
                .orElseThrow(() -> new IllegalArgumentException("추천 기록을 찾을 수 없습니다."));

        recommendation.acceptRecommendation();
        exerciseRecommendationRepository.save(recommendation);
    }
}
