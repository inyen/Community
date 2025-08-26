package com.example.community.board;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;

import java.util.Optional;
/*
JpaRepository 기반 영속성, 컨트롤러 의지
 */
public interface BoardRepository extends JpaRepository<Board, Long> {
    // 목록: 삭제 안된 글 + 작성자 미리 로딩
    @EntityGraph(attributePaths = "user")
    Page<Board> findByDeleteYn(String deleteYn, Pageable pageable);

    //상세: 동일하게 작성자 미리 로딩
    @EntityGraph(attributePaths = "user")
    Optional<Board> findByIdAndDeleteYn(Long id, String deleteYn);
}