package com.example.community.user;

import com.example.community.common.PasswordService;
import com.example.community.user.dto.MeDtos;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MeService {
    private final UserRepository userRepository;
    private final PasswordService passwordService;

    @Transactional(readOnly = true)
    public MeDtos.ProfileResponse getProfile(Long id) {
        User user = userRepository.findActiveById(id)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));
        return new MeDtos.ProfileResponse(user.getUserId(), user.getUserName());
    }

    @Transactional
    public void changeUserName(Long id, MeDtos.UpdateUserNameRequest req){
        if(userRepository.existsByUserName(req.userName())){
            throw new IllegalStateException("이미 사용 중인 닉네임입니다.");
        }
        User user = userRepository.findActiveById(id)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));
        user.setUserName(req.userName());
    }

    @Transactional
    public void changePassword(Long id, MeDtos.ChangePasswordRequest req) {
        User user = userRepository.findActiveById(id)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));
        if (!passwordService.matches(req.currentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }
        user.setPassword(passwordService.encode(req.newPassword()));
    }

    @Transactional
    public void withdraw(Long id, MeDtos.WithdrawRequest req) {
        User user = userRepository.findActiveById(id)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));
        if(!passwordService.matches(req.currentPassword(), user.getPassword())){
            throw new IllegalStateException("현재 비밀번호가 일치하지 않습니다.");
        }
        user.softDelete(); //deleteYn = 'Y'
    }
}