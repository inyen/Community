package com.example.community.user;

import com.example.community.common.PasswordService;
import com.example.community.user.dto.MyDtos;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/*
내 정보 비즈니스 로직
 */
@Service
@RequiredArgsConstructor
public class MyService {
    private final UserRepository userRepository;
    private final PasswordService passwordService;

    //활성 사용자 조회
    @Transactional(readOnly = true)
    public MyDtos.ProfileResponse getProfile(Long id) {
        User user = userRepository.findActiveById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
        return new MyDtos.ProfileResponse(user.getUserId(), user.getUserName());
    }

    //닉네임 중복 검사
    @Transactional
    public void changeUserName(Long id, MyDtos.UpdateUserNameRequest req){
        if(userRepository.existsByUserName(req.userName())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다.");
        }
        User user = userRepository.findActiveById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
        user.setUserName(req.userName());
    }

    //현재 비밀번호 검증 후 새 비밀번호 해시로 교체
    @Transactional
    public void changePassword(Long id, MyDtos.ChangePasswordRequest req) {
        User user = userRepository.findActiveById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
        if (!passwordService.matches(req.currentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "현재 비밀번호가 일치하지 않습니다.");
        }
        user.setPassword(passwordService.encode(req.newPassword()));
    }

    //현재 비밀번호 검증 후 계정 삭제(softDelete)
    @Transactional
    public void withdraw(Long id, MyDtos.WithdrawRequest req) {
        User user = userRepository.findActiveById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
        if(!passwordService.matches(req.currentPassword(), user.getPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "현재 비밀번호가 일치하지 않습니다.");
        }
        user.softDelete(); //deleteYn = 'Y'
    }
}