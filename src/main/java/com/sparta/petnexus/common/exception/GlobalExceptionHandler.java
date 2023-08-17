package com.sparta.petnexus.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 비즈니스 로직 실행 중 에러 발생하는 경우
    @ExceptionHandler(value = BusinessException.class)
    protected ResponseEntity<ErrorResponseDto> handleBusinessException(BusinessException e) {
        log.error("BusinessException", e);
        ErrorResponseDto errorResponse = ErrorResponseDto.of(e.getErrorCode().getCode(),
                e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(errorResponse);
    }

    // 회원가입 valid 에러 발생하는 경우
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponseDto> handleValidationException(
            MethodArgumentNotValidException e) {
        log.error("ValidationException", e);
        ErrorResponseDto errorResponse = ErrorResponseDto.of(HttpStatus.BAD_REQUEST.toString(),
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
