package com.example.community.user.dto;

import jakarta.validation.constraints.NotBlank;
/*
로그인 응답 DTO
 */
public class LoginDtos {
    //엔티티의 로그인 아디이명(userId)
    public record LoginRequest(@NotBlank String userId, @NotBlank String password) {}
    //id=PK, userId=로그인아이디, userName=닉네임
    public record LoginResponse(Long id, String userId, String userName) {}
}