package com.example.community.common.error;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        var fields = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> ErrorResponse.FieldError.of(fe.getField(), fe.getDefaultMessage()))
                .toList();

        var body = ErrorResponse.of(HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR", "요청값이 유효하지 않습니다.", fields);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // 유니크 제약 등 DB 무결성 위반
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
        var body = ErrorResponse.of(HttpStatus.CONFLICT,
                "DATA_INTEGRITY_VIOLATION", "데이터 무결성 제약 위반(중복 또는 형식 오류)입니다.", List.of());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    // 컨트롤러에서 명시적으로 던진 상태 예외
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleRSE(ResponseStatusException ex) {
        var status = ex.getStatusCode();
        var httpStatus = HttpStatus.valueOf(status.value());
        var body = ErrorResponse.of(httpStatus,
                "ERROR", ex.getReason() != null ? ex.getReason() : httpStatus.getReasonPhrase(), List.of());
        return ResponseEntity.status(httpStatus).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleEtc(Exception ex) {
        var body = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_ERROR", "서버 오류가 발생했습니다.", List.of());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}