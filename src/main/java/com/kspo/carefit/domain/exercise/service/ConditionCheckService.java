package com.kspo.carefit.domain.exercise.service;

import com.kspo.carefit.damain.user.entity.User;
import com.kspo.carefit.domain.exercise.entity.ConditionCheck;
import com.kspo.carefit.domain.exercise.enums.ConditionType;
import com.kspo.carefit.domain.exercise.repository.ConditionCheckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class ConditionCheckService {

    private final ConditionCheckRepository conditionCheckRepository;

    /**
     * 컨디션 체크 저장
     */
    public ConditionCheck saveCondition(User user, LocalDate checkDate, ConditionType conditionType) {
        ConditionCheck conditionCheck = ConditionCheck.builder()
                .user(user)
                .checkDate(checkDate)
                .conditionType(conditionType)
                .build();

        return conditionCheckRepository.save(conditionCheck);
    }
}
