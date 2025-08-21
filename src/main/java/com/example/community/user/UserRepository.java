package com.example.community.user;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //회원가입/중복 검사 & 로그인
    boolean existsByUserId(String userId);
    Optional<User> findByUserId(String userId);

    //닉네임 중복 체크
    boolean existsByUserName(String userName);

    // 로그인: 탈퇴하지 않은 계정만
    @Query("select u from User u where u.userId = :userId and u.deleteYn = 'N'")
    Optional<User> findActiveByUserId(@Param("userId") String userId);

    // 마이페이지 등: 세션 보유자도 탈퇴 여부 확인
    @Query("select u from User u where u.id = :id and u.deleteYn = 'N'")
    Optional<User> findActiveById(@Param("id") Long id);
}