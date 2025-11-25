package com.kspo.carefit.domain.exercise.repository;

import com.kspo.carefit.domain.exercise.entity.ConditionCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConditionCheckRepository extends JpaRepository<ConditionCheck, Long> {

    // 특정 날짜의 컨디션 체크 조회
    Optional<ConditionCheck> findByUserIdAndCheckDate(Long userId, LocalDate checkDate);

    // 기간별 컨디션 체크 조회
    List<ConditionCheck> findByUserIdAndCheckDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    // 사용자의 최근 컨디션 체크 조회
    List<ConditionCheck> findByUserIdOrderByCheckDateDesc(Long userId);
}
