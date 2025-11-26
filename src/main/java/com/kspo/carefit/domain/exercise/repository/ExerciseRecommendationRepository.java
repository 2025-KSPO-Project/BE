package com.kspo.carefit.domain.exercise.repository;

import com.kspo.carefit.domain.exercise.entity.ExerciseRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRecommendationRepository extends JpaRepository<ExerciseRecommendation, Long> {

    // 특정 날짜의 추천 기록 조회
    Optional<ExerciseRecommendation> findByUserIdAndRecommendationDate(Long userId, LocalDate recommendationDate);

    // 기간별 추천 기록 조회
    List<ExerciseRecommendation> findByUserIdAndRecommendationDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    // 수락된 추천 기록 조회
    List<ExerciseRecommendation> findByUserIdAndIsAcceptedTrue(Long userId);

    // 사용자의 모든 추천 기록 조회 (최신순)
    List<ExerciseRecommendation> findByUserIdOrderByRecommendationDateDesc(Long userId);
}
