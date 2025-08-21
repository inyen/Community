package com.example.community.auth;

import com.example.community.common.PasswordService;
import com.example.community.common.SessionConst;
import com.example.community.user.User;
import com.example.community.user.UserRepository;
import com.example.community.user.dto.LoginDtos;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordService passwordService;

    @PostMapping("/login")
    public ResponseEntity<LoginDtos.LoginResponse> login(
            @Valid @RequestBody LoginDtos.LoginRequest req, HttpSession session) {
        //아이디 존재 여부 확인
        User user = userRepository.findByUserId(req.userId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "존재하지 않는 아이디입니다."));

        //탈퇴 여부 분기
        if("Y".equals(user.getDeleteYn())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "탈퇴한 계정입니다.");
        }

        //비밀번호 검증
        if(!passwordService.matches(req.password(), user.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        //세션 저장 & 응답
        session.setAttribute(SessionConst.LOGIN_USER_ID, user.getId());
        return ResponseEntity.ok(
                new LoginDtos.LoginResponse(user.getId(), user.getUserId(), user.getUserName()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(HttpSession session) {
        Object id = session.getAttribute(SessionConst.LOGIN_USER_ID);
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        return ResponseEntity.ok(Map.of("userId", id));
    }
}
