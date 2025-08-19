package com.example.community.board;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @EntityGraph(attributePaths = "user")
    Page<Board> findByDeleteYn(String deleteYn, Pageable pageable);

    //상세 조회용
    @EntityGraph(attributePaths = "user")
    Optional<Board> findByIdAndDeleteYn(Long id, String deleteYn);

    //권한 체크용
    //boolean existsByIdAndUser_IdAndDeleteYn(Long id, Long userId, String deleteYn);
}
