package com.kspo.carefit.domain.exercise.service;

import com.kspo.carefit.damain.user.entity.User;
import com.kspo.carefit.domain.exercise.entity.ConditionCheck;
import com.kspo.carefit.domain.exercise.enums.ConditionType;
import com.kspo.carefit.domain.exercise.repository.ConditionCheckRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("ConditionCheckService 단위 테스트")
class ConditionCheckServiceTest {

    @Mock
    private ConditionCheckRepository conditionCheckRepository;

    @InjectMocks
    private ConditionCheckService conditionCheckService;

    private User testUser;
    private ConditionCheck testConditionCheck;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@test.com")
                .build();

        testConditionCheck = ConditionCheck.builder()
                .id(1L)
                .user(testUser)
                .checkDate(LocalDate.now())
                .conditionType(ConditionType.NONE)
                .build();
    }

    @Test
    @DisplayName("컨디션 체크 저장 - 성공")
    void saveCondition_Success() {
        // given
        given(conditionCheckRepository.save(any(ConditionCheck.class)))
                .willReturn(testConditionCheck);

        // when
        ConditionCheck result = conditionCheckService.saveCondition(
                testUser,
                LocalDate.now(),
                ConditionType.NONE
        );

        // then
        assertThat(result).isNotNull();
        assertThat(result.getConditionType()).isEqualTo(ConditionType.NONE);
        assertThat(result.getCheckDate()).isEqualTo(LocalDate.now());
        verify(conditionCheckRepository).save(any(ConditionCheck.class));
    }

    @Test
    @DisplayName("컨디션 체크 저장 - 전날 운동했음")
    void saveCondition_ExercisedYesterday() {
        // given
        ConditionCheck exercisedCondition = ConditionCheck.builder()
                .id(2L)
                .user(testUser)
                .checkDate(LocalDate.now())
                .conditionType(ConditionType.EXERCISED_YESTERDAY)
                .build();

        given(conditionCheckRepository.save(any(ConditionCheck.class)))
                .willReturn(exercisedCondition);

        // when
        ConditionCheck result = conditionCheckService.saveCondition(
                testUser,
                LocalDate.now(),
                ConditionType.EXERCISED_YESTERDAY
        );

        // then
        assertThat(result).isNotNull();
        assertThat(result.getConditionType()).isEqualTo(ConditionType.EXERCISED_YESTERDAY);
    }

    @Test
    @DisplayName("컨디션 체크 저장 - 부상 있음")
    void saveCondition_Injured() {
        // given
        ConditionCheck injuredCondition = ConditionCheck.builder()
                .id(3L)
                .user(testUser)
                .checkDate(LocalDate.now())
                .conditionType(ConditionType.INJURED)
                .build();

        given(conditionCheckRepository.save(any(ConditionCheck.class)))
                .willReturn(injuredCondition);

        // when
        ConditionCheck result = conditionCheckService.saveCondition(
                testUser,
                LocalDate.now(),
                ConditionType.INJURED
        );

        // then
        assertThat(result).isNotNull();
        assertThat(result.getConditionType()).isEqualTo(ConditionType.INJURED);
    }
}
