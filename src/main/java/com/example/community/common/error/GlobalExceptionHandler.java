package com.example.community.common.error;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
/*
컨트롤러 전역 예외 처리
(RestControllerAdvice 예외 -> Http Status 표준 에러 응답으로 변환)
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    //DTO @Valid 검증 실패 -> 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex){
        var fields = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> ErrorResponse.FieldError.of(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        var body = ErrorResponse.of(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "검증값이 유효하지 않습니다.", fields);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    //DB 무결성 제약 위반 -> 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(DataIntegrityViolationException ex){
        var body = ErrorResponse.of(HttpStatus.CONFLICT, "DATA_INTEGRITY_VIOLATION",
                "데이터 무결성 제약 위반(중복 등)", List.of());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    //우리가 던진 ResponseStatusException(404/401/403 등) 그대로 표시
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleRSE(ResponseStatusException ex) {
        // ex에서 HttpStatusCode(401/404)를 받아서 HttpStatus로 변환
        HttpStatus httpStatus = HttpStatus.valueOf(ex.getStatusCode().value());
        String reason = (ex.getReason() != null) ? ex.getReason() : httpStatus.getReasonPhrase();

        // ErrorResponse.of(...)가 HttpStatus를 받는 시그니처인 경우
        ErrorResponse body = ErrorResponse.of(httpStatus, "ERROR", reason, List.of());

        return ResponseEntity.status(httpStatus).body(body);
    }

    //나머지 모든 예외 -> 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleEtc(Exception ex){
        ex.printStackTrace(); //서버 콘솔에 로그 출력
        var body = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_ERROR", "서버에 오류가 발생했습니다.", List.of());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}