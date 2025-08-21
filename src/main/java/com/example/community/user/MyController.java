package com.example.community.user;

import com.example.community.common.SessionConst;
import com.example.community.user.dto.MyDtos;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/me")
public class MyController {
    private final MyService meService;

    private Long currentUserId(HttpSession session) {
        Object uid = session.getAttribute(SessionConst.LOGIN_USER_ID);
        if (uid == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"로그인이 필요합니다.");
        return (Long) uid;
    }

    @GetMapping
    public ResponseEntity<MyDtos.ProfileResponse> profile(HttpSession session) {
        return ResponseEntity.ok(meService.getProfile(currentUserId(session)));
    }

    @PutMapping("/username")
    public ResponseEntity<Void> changeUserName(HttpSession session,
                                               @Valid @RequestBody MyDtos.UpdateUserNameRequest req) {
        meService.changeUserName(currentUserId(session), req);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(HttpSession session,
                                               @Valid @RequestBody MyDtos.ChangePasswordRequest req) {
        meService.changePassword(currentUserId(session), req);
        //비밀번호 변경 후 재로그인 요구
        session.invalidate();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> withdraw(HttpSession session,
                                         @Valid @RequestBody MyDtos.WithdrawRequest req) {
        meService.withdraw(currentUserId(session), req);
        session.invalidate();
        return ResponseEntity.noContent().build();
    }
}