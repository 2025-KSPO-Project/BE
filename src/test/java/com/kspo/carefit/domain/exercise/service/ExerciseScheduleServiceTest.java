package com.kspo.carefit.domain.exercise.service;

import com.kspo.carefit.base.config.exception.domain.BaseException;
import com.kspo.carefit.damain.user.entity.User;
import com.kspo.carefit.domain.exercise.entity.ExerciseSchedule;
import com.kspo.carefit.domain.exercise.repository.ExerciseScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
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
@DisplayName("ExerciseScheduleService 단위 테스트")
class ExerciseScheduleServiceTest {

    @Mock
    private ExerciseScheduleRepository exerciseScheduleRepository;

    @InjectMocks
    private ExerciseScheduleService exerciseScheduleService;

    private User testUser;
    private ExerciseSchedule testSchedule;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@test.com")
                .build();

        testSchedule = ExerciseSchedule.builder()
                .id(1L)
                .user(testUser)
                .exerciseName("수영")
                .scheduledDate(LocalDate.now().plusDays(1))
                .scheduledTime(LocalTime.of(14, 0))
                .durationMinutes(60)
                .notes("수영장 예약 완료")
                .isCompleted(false)
                .build();
    }

    @Test
    @DisplayName("일정 생성 - 성공")
    void createSchedule_Success() {
        // given
        given(exerciseScheduleRepository.save(any(ExerciseSchedule.class)))
                .willReturn(testSchedule);

        // when
        ExerciseSchedule result = exerciseScheduleService.createSchedule(
                testUser,
                "수영",
                LocalDate.now().plusDays(1),
                LocalTime.of(14, 0),
                60,
                "수영장 예약 완료"
        );

        // then
        assertThat(result).isNotNull();
        assertThat(result.getExerciseName()).isEqualTo("수영");
        assertThat(result.getDurationMinutes()).isEqualTo(60);
        verify(exerciseScheduleRepository).save(any(ExerciseSchedule.class));
    }

    @Test
    @DisplayName("날짜별 일정 조회 - 성공")
    void getSchedulesByDate_Success() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        List<ExerciseSchedule> schedules = Arrays.asList(testSchedule);
        given(exerciseScheduleRepository.findByUserIdAndScheduledDate(1L, date))
                .willReturn(schedules);

        // when
        List<ExerciseSchedule> result = exerciseScheduleService.getSchedulesByDate(1L, date);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getExerciseName()).isEqualTo("수영");
    }

    @Test
    @DisplayName("일정 수정 - 성공")
    void updateSchedule_Success() {
        // given
        given(exerciseScheduleRepository.findById(1L)).willReturn(Optional.of(testSchedule));
        given(exerciseScheduleRepository.save(any(ExerciseSchedule.class)))
                .willReturn(testSchedule);

        // when
        ExerciseSchedule result = exerciseScheduleService.updateSchedule(
                1L,
                "수영",
                LocalDate.now().plusDays(2),
                LocalTime.of(15, 0),
                90,
                "시간 변경"
        );

        // then
        assertThat(result).isNotNull();
        verify(exerciseScheduleRepository).save(any(ExerciseSchedule.class));
    }

    @Test
    @DisplayName("일정 수정 - 존재하지 않는 일정일 때 예외 발생")
    void updateSchedule_NotFound_ThrowsException() {
        // given
        given(exerciseScheduleRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> exerciseScheduleService.updateSchedule(
                999L,
                "수영",
                LocalDate.now(),
                LocalTime.of(14, 0),
                60,
                "notes"
        )).isInstanceOf(BaseException.class);
    }

    @Test
    @DisplayName("일정 삭제 - 성공")
    void deleteSchedule_Success() {
        // given
        given(exerciseScheduleRepository.findById(1L)).willReturn(Optional.of(testSchedule));

        // when
        exerciseScheduleService.deleteSchedule(1L);

        // then
        verify(exerciseScheduleRepository).delete(testSchedule);
    }

    @Test
    @DisplayName("일정 삭제 - 존재하지 않는 일정일 때 예외 발생")
    void deleteSchedule_NotFound_ThrowsException() {
        // given
        given(exerciseScheduleRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> exerciseScheduleService.deleteSchedule(999L))
                .isInstanceOf(BaseException.class);
    }
}
