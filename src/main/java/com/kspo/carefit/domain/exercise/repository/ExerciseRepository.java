package com.kspo.carefit.domain.exercise.repository;

import com.kspo.carefit.domain.exercise.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    // 진행 중인 운동 조회
    boolean existsByUserIdAndEndTimeIsNull(Long userId);
    Optional<Exercise> findByUserIdAndEndTimeIsNull(Long userId);

    // 특정 날짜 운동 조회
    List<Exercise> findByUserIdAndExerciseDate(Long userId, LocalDate exerciseDate);

    // 기간별 운동 조회
    List<Exercise> findByUserIdAndExerciseDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    // 사용자의 모든 운동 조회 (최신순)
    List<Exercise> findByUserIdOrderByExerciseDateDescStartTimeDesc(Long userId);
}
