package com.example.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST exception handlers defined at a global level for the application
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    protected ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, HttpStatus.valueOf(apiError.getHttpCode()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex.getMessage());
        return buildResponseEntity(getApiError(ex));
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(getApiError(ex));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(ex.getMessage(), ex);
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }
        return buildResponseEntity(getApiError(ex));
    }

    @ExceptionHandler(RecordNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(RecordNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return buildResponseEntity(getApiError(ex));
    }


    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return buildResponseEntity(getApiError(ex));
    }

    /**
     * This is will used to get an ApiError.
     *
     * @param throwable the exception which had occurred
     * @return {@link ApiError}
     */
    public static ApiError getApiError(Throwable throwable) {
        ApiError apiError;
        if (throwable instanceof MissingServletRequestParameterException) {
            apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), ErrorCode.BAD_REQUEST, throwable.getMessage());
        } else if (throwable instanceof ServletRequestBindingException) {
            apiError = new ApiError(HttpStatus.FORBIDDEN.value(), ErrorCode.INVALID_HEADERS, HttpStatus.FORBIDDEN.getReasonPhrase());
        } else if (throwable instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) throwable;
            BindingResult bindingResult = exception.getBindingResult();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            List<ApiSubError> apiValidationErrors = populateFieldErrors(fieldErrors);
            String fields = fieldErrors.stream()
                    .map(FieldError::getField)
                    .collect(Collectors.joining(","));
            apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), ErrorCode.BAD_REQUEST, "Invalid field(s): " + fields);
            apiError.setSubErrors(apiValidationErrors);
        } else if (throwable instanceof RecordNotFoundException
                || throwable instanceof ClassNotFoundException
                || throwable instanceof NoSuchFileException) {
            apiError = new ApiError(HttpStatus.NOT_FOUND.value(), ErrorCode.NOT_FOUND, throwable.getMessage());
        } else {
            apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.API_ERROR, throwable.getMessage());
        }
        return apiError;
    }

    private static List<ApiSubError> populateFieldErrors(List<FieldError> fieldErrorList) {
        List<ApiSubError> fieldErrors = new ArrayList<>();

        for (FieldError fieldError : fieldErrorList) {
            fieldErrors.add(new ApiValidationError(fieldError.getObjectName(), fieldError.getField(),
                    fieldError.getRejectedValue(), fieldError.getDefaultMessage()));
        }

        return fieldErrors;
    }
}
