package com.example.community.common;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
/*
비밀번호 해시/검증 캡슐화
해시: BCrypt
검증: 해시 형태($2a/$2b/$2y)면 BCrypt, 아니면 평문 비교
 */
@Component
public class PasswordService {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    //비밀번호 원문 -> 해시
    public String encode(String raw) { return encoder.encode(raw); }

    //비밀번호 원문과 해시 저장값 일치 여부 확인
    public boolean matches(String raw, String stored){
        if(stored == null) return false;
        boolean bcrypt = stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$");
        return bcrypt ? encoder.matches(raw, stored) : raw.equals(stored);
    }
}