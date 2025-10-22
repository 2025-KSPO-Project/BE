package com.kspo.carefit.base.config.exception;

import static com.kspo.carefit.base.config.exception.BaseExceptionEnum.EXCEPTION_VALIDATION;

import com.kspo.carefit.base.config.exception.domain.BadRequestException;
import com.kspo.carefit.base.config.exception.domain.BaseDataException;
import com.kspo.carefit.base.config.exception.domain.BaseException;
import com.kspo.carefit.base.config.exception.domain.ForbiddenException;
import com.kspo.carefit.base.config.exception.dto.ApiResult;
import java.text.MessageFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    private static final String LOG_FORMAT = "Class : {}, Code : {}, Message : {}";
    private static final String INTERNAL_SERVER_ERROR_CODE = BaseExceptionEnum.EXCEPTION_ISSUED.getCode();

    /*
        애플리케이션에서 만든 에러처리(RuntimeException)
    */
    @ExceptionHandler(BaseException.class)
    public ApiResult<Void> baseExceptionHandler(BaseException e) {
        log.warn(LOG_FORMAT, e.getClass().getSimpleName(), e.getErrorCode(), e.getMessage());
        return ApiResult.fail(e.getErrorCode(), e.getMessage());
    }

    /*
        애플리케이션에서 만든 에러처리 - data 포함 (RuntimeException)
     */
    @ExceptionHandler(BaseDataException.class)
    public ApiResult<Object> baseExceptionHandler(BaseDataException e) {
        log.warn(LOG_FORMAT, e.getClass().getSimpleName(), e.getErrorCode(), e.getMessage());
        return ApiResult.fail(e.getErrorCode(), e.getMessage(), e.getData());
    }

    /*
        애플리케이션에서 만든 에러처리(RuntimeException)
    */
    @ExceptionHandler(RuntimeException.class)
    public ApiResult<Void> runtimeExceptionHandler(RuntimeException e) {
        log.warn(LOG_FORMAT, e.getClass().getSimpleName(), INTERNAL_SERVER_ERROR_CODE, e.getMessage());
        return ApiResult.fail(BaseExceptionEnum.EXCEPTION_ISSUED);
    }

    /*
        애플리케이션에서 지정하지 않은 (예상치 못한) 에러처리(Exception)
    */
    @ExceptionHandler(Exception.class)
    public ApiResult<Void> exceptionHandler(Exception e) {
        log.error(LOG_FORMAT, e.getClass().getSimpleName(), INTERNAL_SERVER_ERROR_CODE, e.getMessage());
        return ApiResult.fail(BaseExceptionEnum.EXCEPTION_ISSUED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String errorMessage = "";

        if (bindingResult instanceof FieldError) {
            FieldError field = (FieldError) bindingResult.getAllErrors().get(0);
            errorMessage = MessageFormat.format("{0} - {1}", field.getField(), field.getDefaultMessage());
        } else if (bindingResult instanceof ObjectError) {
            ObjectError object = bindingResult.getAllErrors().get(0);
            errorMessage = MessageFormat.format("{0} - {1}", object.getObjectName(), object.getDefaultMessage());
        } else if (bindingResult instanceof BeanPropertyBindingResult) {
            if(bindingResult.getAllErrors().get(0) instanceof FieldError) {
                FieldError field = (FieldError) bindingResult.getAllErrors().get(0);
                errorMessage = MessageFormat.format("{0} - {1}", field.getField(), field.getDefaultMessage());
            }else{
                ObjectError object = bindingResult.getAllErrors().get(0);
                errorMessage = MessageFormat.format("{0}", object.getDefaultMessage());
            }


        }

        log.warn(LOG_FORMAT, e.getClass().getSimpleName(), EXCEPTION_VALIDATION, errorMessage);
        return ApiResult.fail(EXCEPTION_VALIDATION.getCode(), errorMessage);
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public ApiResult<Void> forbiddenExceptionHandler(ForbiddenException e) {
        log.warn(LOG_FORMAT, e.getClass().getSimpleName(), BaseExceptionEnum.FORBIDDEN, e.getMessage());
        return ApiResult.fail(e.getErrorCode(), e.getMessage());
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ApiResult<Void> forbiddenExceptionHandler(AccessDeniedException e) {
        BaseExceptionEnum forbidden = BaseExceptionEnum.FORBIDDEN;
        log.warn(LOG_FORMAT, e.getClass().getSimpleName(), forbidden, e.getMessage());
        return ApiResult.fail(forbidden.getCode(), "AccessDeniedException");
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ApiResult<Void> handleIllegalArgumentException(InvalidDataAccessApiUsageException e) {
        String message = e.getMessage();
        log.error(LOG_FORMAT, e.getClass().getSimpleName(), BaseExceptionEnum.EXCEPTION_ISSUED, e.getMessage());

        if (message.contains("The given id must not be null") // JPA ID null
                || message.contains("eq(null) is not allowed. Use isNull() instead")) { // querydsl id null
            return ApiResult.fail(BaseExceptionEnum.ENTITY_NOT_FOUND);
        }

        return ApiResult.fail(BaseExceptionEnum.EXCEPTION_ISSUED);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResult<Void> methodArgumentTypeMismatchExceptionException(MethodArgumentTypeMismatchException e) {
        BaseExceptionEnum entityNotFound = BaseExceptionEnum.ENTITY_NOT_FOUND;
        log.error(LOG_FORMAT, e.getClass().getSimpleName(), entityNotFound, e.getMessage());
        return ApiResult.fail(entityNotFound);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ApiResult<Void> badRequestExceptionHandler(BadRequestException e) {
        log.error(LOG_FORMAT, e.getClass().getSimpleName(), BaseExceptionEnum.BAD_REQUEST, e.getMessage());
        return ApiResult.fail(e.getErrorCode(), e.getMessage());
    }

}
