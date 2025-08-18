package com.example.community.board;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @EntityGraph(attributePaths = "user")
    Page<Board> findByDeleteYn(String deleteYn, Pageable pageable);
}
