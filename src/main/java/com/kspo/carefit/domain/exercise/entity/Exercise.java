package com.kspo.carefit.domain.exercise.entity;

import com.kspo.carefit.base.jpa.BaseEntity;
import com.kspo.carefit.base.jpa.converter.BooleanToYNConverter;
import com.kspo.carefit.damain.user.entity.User;
import com.kspo.carefit.domain.exercise.enums.ConditionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "exercise", indexes = {
        @Index(name = "idx_user_date", columnList = "user_id, exercise_date"),
        @Index(name = "idx_user_end_time", columnList = "user_id, end_time")
})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Exercise extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "exercise_name", nullable = false, length = 100)
    private String exerciseName;

    @Column(name = "exercise_date", nullable = false)
    private LocalDate exerciseDate;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "condition_status", length = 50)
    @Enumerated(EnumType.STRING)
    private ConditionType conditionStatus;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "is_from_schedule")
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isFromSchedule;

    @Column(name = "is_recommended")
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isRecommended;

    // 비즈니스 로직
    public void endExercise(LocalDateTime endTime, String notes) {
        this.endTime = endTime;
        this.notes = notes;
        this.durationMinutes = calculateDuration(this.startTime, endTime);
    }

    public void updateNotes(String notes) {
        this.notes = notes;
    }

    private int calculateDuration(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return 0;
        }
        return (int) Duration.between(start, end).toMinutes();
    }

    public boolean isOngoing() {
        return this.endTime == null;
    }
}
