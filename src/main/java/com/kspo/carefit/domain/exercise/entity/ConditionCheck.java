package com.kspo.carefit.domain.exercise.entity;

import com.kspo.carefit.base.jpa.BaseEntity;
import com.kspo.carefit.damain.user.entity.User;
import com.kspo.carefit.domain.exercise.enums.ConditionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "condition_check", indexes = {
        @Index(name = "idx_user_check_date", columnList = "user_id, check_date")
})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ConditionCheck extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "check_date", nullable = false)
    private LocalDate checkDate;

    @Column(name = "condition_type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ConditionType conditionType;
}
