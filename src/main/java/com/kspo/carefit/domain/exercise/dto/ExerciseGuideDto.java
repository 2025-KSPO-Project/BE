package com.kspo.carefit.domain.exercise.dto;

import com.kspo.carefit.domain.exercise.enums.ConditionType;
import com.kspo.carefit.domain.exercise.enums.GuideType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ExerciseGuideDto {

    public record Request(
            String sportName,
            ConditionType conditionType,
            GuideType guideType,
            String additionalNotes
    ) {}

    public record Response(
            GuideType guideType,
            String title,
            String content,
            List<String> checklist,
            String caution,
            Integer estimatedMinutes
    ) {
        public static Response of(GuideType guideType, Map<String, String> llmResponse) {
            return new Response(
                    guideType,
                    llmResponse.getOrDefault("title", getDefaultTitle(guideType)),
                    llmResponse.getOrDefault("content", ""),
                    parseChecklist(llmResponse.getOrDefault("checklist", "")),
                    llmResponse.getOrDefault("caution", ""),
                    parseMinutes(llmResponse.getOrDefault("estimated_minutes", "10"))
            );
        }

        private static String getDefaultTitle(GuideType guideType) {
            return switch (guideType) {
                case PRE -> "운동 전 준비사항";
                case DURING -> "운동 중 주의사항";
                case POST -> "운동 후 회복 가이드";
            };
        }

        private static List<String> parseChecklist(String checklist) {
            if (checklist == null || checklist.isBlank()) {
                return Collections.emptyList();
            }
            return Arrays.stream(checklist.split("[,\\n]"))
                    .map(String::trim)
                    .filter(s -> !s.isBlank())
                    .toList();
        }

        private static Integer parseMinutes(String minutes) {
            try {
                return Integer.parseInt(minutes.replaceAll("[^0-9]", ""));
            } catch (NumberFormatException e) {
                return 10;
            }
        }
    }
}
