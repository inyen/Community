package com.example.community.common.error;


import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import java.util.List;
/*
에러 응답 표준 포맷
 */
public record ErrorResponse (
        String code,    //내부 에러 코드
        String message,     //사용자에게 보여줄 메시지
        int status,     // HTTP 상태 코드
        LocalDateTime timestamp,    //서버 기준 발생 시각
        List<FieldError> fieldErrors    //필드 검증 실패 정보
) {
    public static ErrorResponse of(HttpStatus status, String code, String message, List<FieldError> fieldErrors) {
        return new ErrorResponse(code, message, status.value(), LocalDateTime.now(), fieldErrors);
    }

    public record FieldError(String field, String reason) {
        public static FieldError of(String field, String reason) {return new FieldError(field, reason);}
    }
}