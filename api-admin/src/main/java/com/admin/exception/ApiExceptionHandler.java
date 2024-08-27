package com.admin.exception;

import com.admin.exception.response.ErrorResponse;
import com.admin.exception.type.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    // 커스텀 예외 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorResponse error = new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
        error.setCode(errorCode.getCode());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}