package com.kspo.carefit.domain.exercise.repository;

import com.kspo.carefit.domain.exercise.entity.ExerciseSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExerciseScheduleRepository extends JpaRepository<ExerciseSchedule, Long> {

    // 특정 날짜의 일정 조회
    List<ExerciseSchedule> findByUserIdAndScheduledDate(Long userId, LocalDate scheduledDate);

    // 기간별 일정 조회
    List<ExerciseSchedule> findByUserIdAndScheduledDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    // 미완료 일정 조회
    List<ExerciseSchedule> findByUserIdAndIsCompletedFalseOrderByScheduledDateAsc(Long userId);

    // 사용자의 모든 일정 조회 (날짜순)
    List<ExerciseSchedule> findByUserIdOrderByScheduledDateAscScheduledTimeAsc(Long userId);
}
