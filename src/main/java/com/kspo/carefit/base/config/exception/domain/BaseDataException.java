package com.kspo.carefit.base.config.exception.domain;

import com.kspo.carefit.base.enums.MessageCommInterface;

public class BaseDataException extends RuntimeException {

    private final String errorCode;
    private final String message;

    private final Object data;

    public BaseDataException(MessageCommInterface messageCommInterface, Object data) {
        super(messageCommInterface.getMessage());
        this.errorCode = messageCommInterface.getCode();
        this.message = messageCommInterface.getMessage();
        this.data = data;
    }

    public BaseDataException(String errorCode, String message, Object data) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
        this.data = data;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
