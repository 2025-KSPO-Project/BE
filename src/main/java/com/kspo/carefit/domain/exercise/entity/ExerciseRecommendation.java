package com.kspo.carefit.domain.exercise.entity;

import com.kspo.carefit.base.jpa.BaseEntity;
import com.kspo.carefit.base.jpa.converter.BooleanToYNConverter;
import com.kspo.carefit.damain.user.entity.User;
import com.kspo.carefit.domain.exercise.enums.ConditionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "exercise_recommendation", indexes = {
        @Index(name = "idx_user_recommendation_date", columnList = "user_id, recommendation_date")
})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ExerciseRecommendation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "recommendation_date", nullable = false)
    private LocalDate recommendationDate;

    @Column(name = "exercise_name", nullable = false, length = 100)
    private String exerciseName;

    @Column(name = "sport_name", length = 100)
    private String sportName;

    @Column(name = "condition_type", length = 50)
    @Enumerated(EnumType.STRING)
    private ConditionType conditionType;

    @Column(name = "llm_prompt", columnDefinition = "TEXT")
    private String llmPrompt;

    @Column(name = "llm_response", columnDefinition = "TEXT")
    private String llmResponse;

    @Column(name = "is_accepted")
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isAccepted;

    // 비즈니스 로직
    public void acceptRecommendation() {
        this.isAccepted = true;
    }
}
