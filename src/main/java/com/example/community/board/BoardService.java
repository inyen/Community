package com.example.community.board;

import com.example.community.board.dto.BoardDtos;

public interface BoardService {
    BoardDtos.DetailRes getDetail(Long id);
    void update(Long id, Long currentUserId, BoardDtos.UpdateReq req);
    void delete(Long id, Long currentUserId);
}