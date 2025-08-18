package com.example.community.user;

import com.example.community.user.dto.UserDtos;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<UserDtos.CreateRes> create(@Valid @RequestBody UserDtos.CreateReq req) {
        if (userRepository.existsByUserId(req.userId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다.");
        }
        if (userRepository.existsByUserName(req.userName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다.");
        }

        User saved = userRepository.save(User.create(req.userId(), req.password(), req.userName()));

        return ResponseEntity.status(HttpStatus.CREATED).body(UserDtos.CreateRes.from(saved));
    }

}