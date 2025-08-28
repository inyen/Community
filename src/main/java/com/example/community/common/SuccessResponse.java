package com.example.community.common;

public record SuccessResponse(String message) {
    public static SuccessResponse of(String message) {
        return new SuccessResponse(message);
    }
}
