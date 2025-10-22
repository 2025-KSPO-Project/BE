package com.kspo.carefit.base.config.exception.domain;

import com.kspo.carefit.base.config.exception.BaseExceptionEnum;
import com.kspo.carefit.base.enums.MessageCommInterface;
import java.io.Serial;

public class BadRequestException extends BaseException {

    @Serial
    private static final long serialVersionUID = -5148452197821358350L;

    public BadRequestException() {
        super(BaseExceptionEnum.BAD_REQUEST);
    }

    public BadRequestException(MessageCommInterface messageCommInterface) {
        super(messageCommInterface);
    }
}
