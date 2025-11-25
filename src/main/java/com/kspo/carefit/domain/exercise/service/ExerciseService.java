package com.kspo.carefit.domain.exercise.service;

import com.kspo.carefit.base.config.exception.domain.BaseException;
import com.kspo.carefit.damain.user.entity.User;
import com.kspo.carefit.domain.exercise.entity.Exercise;
import com.kspo.carefit.domain.exercise.enums.ConditionType;
import com.kspo.carefit.domain.exercise.exception.ExerciseExceptionEnum;
import com.kspo.carefit.domain.exercise.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    /**
     * 운동 시작
     */
    public Exercise startExercise(User user, String exerciseName, LocalDateTime startTime, ConditionType conditionStatus) {
        // 진행 중인 운동이 있는지 확인
        validateNoOngoingExercise(user.getId());

        Exercise exercise = Exercise.builder()
                .user(user)
                .exerciseName(exerciseName)
                .exerciseDate(startTime.toLocalDate())
                .startTime(startTime)
                .conditionStatus(conditionStatus)
                .isFromSchedule(false)
                .isRecommended(false)
                .build();

        return exerciseRepository.save(exercise);
    }

    /**
     * 운동 종료
     */
    public Exercise endExercise(Long exerciseId, LocalDateTime endTime, String notes) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new BaseException(ExerciseExceptionEnum.EXERCISE_NOT_FOUND));

        // 이미 종료된 운동인지 확인
        if (!exercise.isOngoing()) {
            throw new BaseException(ExerciseExceptionEnum.EXERCISE_NOT_STARTED);
        }

        exercise.endExercise(endTime, notes);
        return exerciseRepository.save(exercise);
    }

    /**
     * 현재 진행 중인 운동 조회
     */
    @Transactional(readOnly = true)
    public Optional<Exercise> getCurrentExercise(Long userId) {
        return exerciseRepository.findByUserIdAndEndTimeIsNull(userId);
    }

    /**
     * 진행 중인 운동이 없는지 검증
     */
    public void validateNoOngoingExercise(Long userId) {
        boolean hasOngoingExercise = exerciseRepository.existsByUserIdAndEndTimeIsNull(userId);
        if (hasOngoingExercise) {
            throw new BaseException(ExerciseExceptionEnum.EXERCISE_ALREADY_STARTED);
        }
    }

    /**
     * 운동 조회 (권한 검증 포함)
     */
    public Exercise getExerciseById(Long exerciseId) {
        return exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new BaseException(ExerciseExceptionEnum.EXERCISE_NOT_FOUND));
    }

    /**
     * 기간별 운동 조회
     */
    @Transactional(readOnly = true)
    public List<Exercise> getExercisesByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return exerciseRepository.findByUserIdAndExerciseDateBetween(userId, startDate, endDate);
    }

    /**
     * 특정 날짜 운동 조회
     */
    @Transactional(readOnly = true)
    public List<Exercise> getExercisesByDate(Long userId, LocalDate date) {
        return exerciseRepository.findByUserIdAndExerciseDate(userId, date);
    }
}
