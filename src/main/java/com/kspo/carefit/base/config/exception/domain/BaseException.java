package com.kspo.carefit.base.config.exception.domain;

import com.kspo.carefit.base.enums.MessageCommInterface;
import java.text.MessageFormat;

public class BaseException extends RuntimeException {

    private final String errorCode;
    private final String message;

    public BaseException(MessageCommInterface messageCommInterface) {
        super(messageCommInterface.getMessage());
        this.errorCode = messageCommInterface.getCode();
        this.message = messageCommInterface.getMessage();
    }

    public BaseException(MessageCommInterface messageCommInterface, Object[] messageParameters) {
        super(messageParameters != null ? MessageFormat.format(messageCommInterface.getMessage(), messageParameters) : messageCommInterface.getMessage());
        this.errorCode = messageCommInterface.getCode();
        this.message = messageParameters != null ? MessageFormat.format(messageCommInterface.getMessage(), messageParameters) : messageCommInterface.getMessage();
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
