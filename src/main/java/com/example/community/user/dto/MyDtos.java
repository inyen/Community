package com.example.community.user.dto;

import jakarta.validation.constraints.*;

public class MyDtos {
    // 프로필 조회 응답
    public record ProfileResponse(
            String userId,
            String userName
    ) {}

    // 닉네임 변경 요청
    public record UpdateUserNameRequest(
            @NotBlank
            @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,8}$",
                    message = "닉네임은 2~8자, 영문/숫자/한글만 허용")
            String userName
    ) {}

    // 비밀번호 변경 요청
    public record ChangePasswordRequest(
            @NotBlank String currentPassword,
            @NotBlank
            @Pattern(
                    regexp = "^[A-Za-z0-9!@#$%^*]{8,15}$",
                    message = "비밀번호는 8~15자, 공백없음, 대소문자/숫자/특수문자(!@#$%^*)를 포함")
            String newPassword
    ) {}

    // 회원 탈퇴 요청
    public record WithdrawRequest(
            @NotBlank String currentPassword,
            @AssertTrue(message = "탈퇴에 동의해야 합니다.") boolean agree
    ) {}
}