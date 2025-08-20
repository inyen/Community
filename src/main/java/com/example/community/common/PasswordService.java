package com.example.community.common;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordService {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String encode(String raw) { return encoder.encode(raw); }

    //평문 저장본을 로그인만 허용(점진 마이그레이션)
    public boolean matches(String raw, String stored){
        if(stored == null) return false;
        boolean bcrypt = stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$");
        return bcrypt ? encoder.matches(raw, stored) : raw.equals(stored);
    }
}
