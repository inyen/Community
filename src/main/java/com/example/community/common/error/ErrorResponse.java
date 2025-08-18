package com.example.community.common.error;


import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse (
        String code,
        String message,
        int status,
        LocalDateTime timestamp,
        List<FieldError> fieldErrors
) {
    public static ErrorResponse of(HttpStatus status, String code, String message, List<FieldError> fieldErrors) {
        return new ErrorResponse(code, message, status.value(), LocalDateTime.now(), fieldErrors);
    }

    public record FieldError(String field, String reason) {
        public static FieldError of(String field, String reason) {return new FieldError(field, reason);}
    }
}
