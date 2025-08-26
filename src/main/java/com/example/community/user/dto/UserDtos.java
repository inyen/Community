package com.example.community.user.dto;

import com.example.community.user.User;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
/*
회원가입 입출력 모델
 */
public class UserDtos {
    public record CreateReq(
            @NotBlank
            @Pattern(regexp = "^[a-z0-9]{4,8}$", message = "아이디는 영문/소문자 4~8자여야 합니다.")
            String userId,

            @NotBlank
            @Pattern(
                    regexp = "^[A-Za-z0-9!@#$%^*]{8,15}$",
                    message = "비밀번호는 8~15자여야 합니다.")
            String password,

            @NotBlank
            @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,8}$", message = "닉네임은 2~8자, 영문/숫자/한글만 가능합니다.")
            String userName
    ) {}
    public record CreateRes(
            Long id,
            String userId,
            String userName,
            LocalDateTime createdTime
    ) {
        public static CreateRes from(User u){
            return new CreateRes(u.getId(), u.getUserId(), u.getUserName(), u.getCreatedTime());
        }
    }

}