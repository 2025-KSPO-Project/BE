package com.kspo.carefit.domain.exercise.service;

import com.kspo.carefit.base.config.exception.domain.BaseException;
import com.kspo.carefit.damain.user.entity.User;
import com.kspo.carefit.domain.exercise.entity.Exercise;
import com.kspo.carefit.domain.exercise.enums.ConditionType;
import com.kspo.carefit.domain.exercise.repository.ExerciseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExerciseService 단위 테스트")
class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private ExerciseService exerciseService;

    private User testUser;
    private Exercise testExercise;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@test.com")
                .build();

        testExercise = Exercise.builder()
                .id(1L)
                .user(testUser)
                .exerciseName("러닝")
                .exerciseDate(LocalDate.now())
                .startTime(LocalDateTime.now())
                .conditionStatus(ConditionType.NONE)
                .isFromSchedule(false)
                .isRecommended(false)
                .build();
    }

    @Test
    @DisplayName("운동 시작 - 성공")
    void startExercise_Success() {
        // given
        given(exerciseRepository.existsByUserIdAndEndTimeIsNull(anyLong())).willReturn(false);
        given(exerciseRepository.save(any(Exercise.class))).willReturn(testExercise);

        // when
        Exercise result = exerciseService.startExercise(
                testUser,
                "러닝",
                LocalDateTime.now(),
                ConditionType.NONE
        );

        // then
        assertThat(result).isNotNull();
        assertThat(result.getExerciseName()).isEqualTo("러닝");
        verify(exerciseRepository).save(any(Exercise.class));
    }

    @Test
    @DisplayName("운동 시작 - 이미 진행 중인 운동이 있을 때 예외 발생")
    void startExercise_AlreadyStarted_ThrowsException() {
        // given
        given(exerciseRepository.existsByUserIdAndEndTimeIsNull(anyLong())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> exerciseService.startExercise(
                testUser,
                "러닝",
                LocalDateTime.now(),
                ConditionType.NONE
        )).isInstanceOf(BaseException.class);
    }

    @Test
    @DisplayName("운동 종료 - 성공")
    void endExercise_Success() {
        // given
        LocalDateTime endTime = LocalDateTime.now().plusHours(1);
        given(exerciseRepository.findById(1L)).willReturn(Optional.of(testExercise));
        given(exerciseRepository.save(any(Exercise.class))).willReturn(testExercise);

        // when
        Exercise result = exerciseService.endExercise(1L, endTime, "5km 완주");

        // then
        assertThat(result).isNotNull();
        verify(exerciseRepository).save(any(Exercise.class));
    }

    @Test
    @DisplayName("운동 종료 - 존재하지 않는 운동일 때 예외 발생")
    void endExercise_NotFound_ThrowsException() {
        // given
        given(exerciseRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> exerciseService.endExercise(
                999L,
                LocalDateTime.now(),
                "notes"
        )).isInstanceOf(BaseException.class);
    }

    @Test
    @DisplayName("현재 진행 중인 운동 조회 - 성공")
    void getCurrentExercise_Success() {
        // given
        given(exerciseRepository.findByUserIdAndEndTimeIsNull(1L))
                .willReturn(Optional.of(testExercise));

        // when
        Optional<Exercise> result = exerciseService.getCurrentExercise(1L);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getExerciseName()).isEqualTo("러닝");
    }

    @Test
    @DisplayName("특정 날짜 운동 조회 - 성공")
    void getExercisesByDate_Success() {
        // given
        LocalDate date = LocalDate.now();
        List<Exercise> exercises = Arrays.asList(testExercise);
        given(exerciseRepository.findByUserIdAndExerciseDate(1L, date))
                .willReturn(exercises);

        // when
        List<Exercise> result = exerciseService.getExercisesByDate(1L, date);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getExerciseName()).isEqualTo("러닝");
    }

    @Test
    @DisplayName("기간별 운동 조회 - 성공")
    void getExercisesByDateRange_Success() {
        // given
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        List<Exercise> exercises = Arrays.asList(testExercise);
        given(exerciseRepository.findByUserIdAndExerciseDateBetween(1L, startDate, endDate))
                .willReturn(exercises);

        // when
        List<Exercise> result = exerciseService.getExercisesByDateRange(1L, startDate, endDate);

        // then
        assertThat(result).hasSize(1);
    }
}
