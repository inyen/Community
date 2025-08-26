package com.example.community.auth;

import com.example.community.common.PasswordService;
import com.example.community.common.SessionConst;
import com.example.community.user.User;
import com.example.community.user.UserRepository;
import com.example.community.user.dto.LoginDtos;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

/*
로그인(세션 생성), 로그아웃(세션 만료) API
*/
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordService passwordService;

    //로그인 인증
    @PostMapping("/login")
    //아이디, 비밀번호 입력 검증
    public ResponseEntity<LoginDtos.LoginResponse> login(
            @Valid @RequestBody LoginDtos.LoginRequest req, HttpSession session) {
        //아이디 존재 여부 확인
        User user = userRepository.findByUserId(req.userId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "존재하지 않는 아이디입니다.")); //NOT_FOUND -> 404, 요청 페이지 못찾음

        //탈퇴 여부 확인
        if("Y".equals(user.getDeleteYn())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "탈퇴한 계정입니다."); //FORBIDDEN -> 403, 권한 부족
        }

        //비밀번호 검증(BCrypt 해시 비교)
        if(!passwordService.matches(req.password(), user.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."); //UNAUTHORIZED -> 401, 유효 자격 증명X
        }

        //세션 저장 & 응답
        //세션에는 PK(id)만 저장
        session.setAttribute(SessionConst.LOGIN_USER_ID, user.getId());
        return ResponseEntity.ok(
                new LoginDtos.LoginResponse(user.getId(), user.getUserId(), user.getUserName()));
    }

    //로그아웃 인증
    @PostMapping("/logout")
    //세션 무효화 -> 서버 측 인증 상태 즉시 종료
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.noContent().build(); //noContent -> 204
    }

    //내 세션 확인
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(HttpSession session) {
        //세션에 사용자 식별자 있는지 확인
        Object id = session.getAttribute(SessionConst.LOGIN_USER_ID);
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        return ResponseEntity.ok(Map.of("userId", id));
    }
}