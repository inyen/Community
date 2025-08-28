package com.example.community.user;

import com.example.community.common.auth.LoginUser;
import com.example.community.user.dto.MyDtos;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
/*
내 정보 조회, 수정, 탈퇴
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class MyController {
    private final MyService myService;

    @GetMapping("/me")
    public ResponseEntity<MyDtos.ProfileResponse> me(@LoginUser Long loginUserId) {
        return profile(loginUserId, loginUserId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MyDtos.ProfileResponse> profile(@PathVariable Long id,
                                                          @LoginUser Long loginUserId) {
        forbidIfNotOwner(id, loginUserId);
        return ResponseEntity.ok(myService.getProfile(id));
    }

    @PatchMapping("/{id}/username")
    public ResponseEntity<Void> changeUserName(@PathVariable Long id,
                                               @LoginUser Long loginUserId,
                                               @Valid @RequestBody MyDtos.UpdateUserNameRequest req) {
        forbidIfNotOwner(id, loginUserId);
        myService.changeUserName(id, req);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id,
                                               @LoginUser Long loginUserId,
                                               @Valid @RequestBody MyDtos.ChangePasswordRequest req,
                                               HttpSession session) {
        forbidIfNotOwner(id, loginUserId);
        myService.changePassword(id, req);
        //비밀번호 변경 후 재로그인 요구(세션 종료)
        session.invalidate();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> withdraw(@PathVariable Long id,
                                         @LoginUser Long loginUserId,
                                         @Valid @RequestBody MyDtos.WithdrawRequest req,
                                         HttpSession session) {
        forbidIfNotOwner(id, loginUserId);
        myService.withdraw(id, req);
        session.invalidate();   //계정 탈퇴 후 세션 종료
        return ResponseEntity.noContent().build();
    }

    private static void forbidIfNotOwner(Long id, Long loginUserId) {
        if(!id.equals(loginUserId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인만 접근 가능합니다.");
    }
}