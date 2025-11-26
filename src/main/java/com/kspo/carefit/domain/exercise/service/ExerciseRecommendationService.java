package com.kspo.carefit.domain.exercise.service;

import com.kspo.carefit.base.client.AIClient;
import com.kspo.carefit.base.client.AIClientFactory;
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
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ExerciseRecommendationService {

    private final ExerciseRecommendationRepository exerciseRecommendationRepository;
    private final AIClientFactory aiClientFactory;

    /**
     * 운동 추천 생성 및 저장
     */
    public ExerciseRecommendation createRecommendation(User user, ConditionType conditionType,
                                                       List<Exercise> recentExercises) {
        LocalDate today = LocalDate.now();

        // LLM 프롬프트 생성
        String llmPrompt = buildLLMPrompt(user, conditionType, recentExercises);

        // AI 클라이언트를 통해 운동 추천 받기
        AIClient aiClient = aiClientFactory.getAIClient();
        Map<String, String> llmResult = aiClient.getExerciseRecommendation(llmPrompt);

        // LLM 응답에서 데이터 추출
        String exerciseName = llmResult.getOrDefault("exercise_name", "걷기");
        String sportName = llmResult.getOrDefault("sport_name", "산책");
        String reason = llmResult.getOrDefault("reason", "컨디션에 맞는 운동을 추천드립니다.");

        ExerciseRecommendation recommendation = ExerciseRecommendation.builder()
                .user(user)
                .recommendationDate(today)
                .exerciseName(exerciseName)
                .sportName(sportName)
                .conditionType(conditionType)
                .llmPrompt(llmPrompt)
                .llmResponse(reason)
                .isAccepted(false)
                .build();

        return exerciseRecommendationRepository.save(recommendation);
    }

    /**
     * LLM 프롬프트 생성 (장애유형 반영)
     */
    private String buildLLMPrompt(User user, ConditionType conditionType, List<Exercise> recentExercises) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("사용자 정보:\n");
        prompt.append("- 장애유형: ").append(getDisabilityName(user.getDisabilityCode())).append("\n");
        prompt.append("- 관심 스포츠 코드: ").append(user.getSportsCode()).append("\n");
        prompt.append("- 오늘 컨디션: ").append(conditionType.getCodeName()).append("\n");
        prompt.append("- 최근 운동 기록:\n");

        if (recentExercises.isEmpty()) {
            prompt.append("  - 최근 운동 기록 없음\n");
        } else {
            recentExercises.forEach(exercise -> {
                prompt.append("  - ").append(exercise.getExerciseDate())
                        .append(": ").append(exercise.getExerciseName())
                        .append(" (").append(exercise.getDurationMinutes()).append("분)\n");
            });
        }

        prompt.append("\n위 정보를 바탕으로 오늘 할 수 있는 적절한 운동을 추천해주세요.\n");
        prompt.append("장애유형에 맞는 안전한 운동을 추천하고, 주의사항도 함께 알려주세요.\n");
        prompt.append("JSON 형식으로 응답해주세요:\n");
        prompt.append("{\n");
        prompt.append("  \"sport_name\": \"추천 스포츠명\",\n");
        prompt.append("  \"exercise_name\": \"추천 운동명\",\n");
        prompt.append("  \"sport_info\": \"운동에 대한 간략한 소개\",\n");
        prompt.append("  \"recommend_time\": \"추천 운동 시간 (분)\",\n");
        prompt.append("  \"reason\": \"추천 이유\",\n");
        prompt.append("  \"caution\": \"장애유형별 주의사항\",\n");
        prompt.append("  \"pre_exercise_tip\": \"운동 전 준비사항\"\n");
        prompt.append("}");

        return prompt.toString();
    }

    /**
     * 장애유형 코드를 이름으로 변환
     */
    private String getDisabilityName(Integer disabilityCode) {
        if (disabilityCode == null) {
            return "정보 없음";
        }
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
     * 추천 수락
     */
    public void acceptRecommendation(Long recommendationId) {
        ExerciseRecommendation recommendation = exerciseRecommendationRepository.findById(recommendationId)
                .orElseThrow(() -> new IllegalArgumentException("추천 기록을 찾을 수 없습니다."));

        recommendation.acceptRecommendation();
        exerciseRecommendationRepository.save(recommendation);
    }
}
