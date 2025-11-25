package com.kspo.carefit.domain.exercise.entity;

import com.kspo.carefit.base.jpa.BaseEntity;
import com.kspo.carefit.base.jpa.converter.BooleanToYNConverter;
import com.kspo.carefit.damain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "exercise_schedule", indexes = {
        @Index(name = "idx_user_scheduled_date", columnList = "user_id, scheduled_date")
})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ExerciseSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "exercise_name", nullable = false, length = 100)
    private String exerciseName;

    @Column(name = "scheduled_date", nullable = false)
    private LocalDate scheduledDate;

    @Column(name = "scheduled_time")
    private LocalTime scheduledTime;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "is_completed")
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isCompleted;

    // 비즈니스 로직
    public void markAsCompleted() {
        this.isCompleted = true;
    }

    public void updateSchedule(String exerciseName, LocalDate scheduledDate, LocalTime scheduledTime, Integer durationMinutes, String notes) {
        this.exerciseName = exerciseName;
        this.scheduledDate = scheduledDate;
        this.scheduledTime = scheduledTime;
        this.durationMinutes = durationMinutes;
        this.notes = notes;
    }

    public boolean isPast(LocalDate currentDate) {
        return this.scheduledDate.isBefore(currentDate);
    }
}
