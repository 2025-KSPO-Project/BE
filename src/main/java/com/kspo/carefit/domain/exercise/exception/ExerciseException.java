package com.kspo.carefit.domain.exercise.exception;

import com.kspo.carefit.base.config.exception.domain.BaseException;
import com.kspo.carefit.base.enums.MessageCommInterface;

public class ExerciseException extends BaseException {

    public ExerciseException(MessageCommInterface messageCommInterface) {
        super(messageCommInterface);
    }

    public ExerciseException(MessageCommInterface messageCommInterface, Object[] messageParameters) {
        super(messageCommInterface, messageParameters);
    }
}
