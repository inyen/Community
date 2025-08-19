package com.example.community.board;

import com.example.community.board.dto.BoardDtos;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Override
    public BoardDtos.DetailRes getDetail(Long id) {
        var board = boardRepository.findByIdAndDeleteYn(id, "N")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
        return BoardDtos.DetailRes.from(board);
    }

    @Override
    @Transactional
    public void update(Long id, Long currentUserId, BoardDtos.UpdateReq req) {
        if (currentUserId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 필요합니다");
        }
        var board = boardRepository.findByIdAndDeleteYn(id, "N")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
        if (!board.getUser().getId().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "작성자만 수정 가능합니다");
        }
        board.applyUpdate(req.title(), req.content()); // 또는 setTitle/setContent
    }

    @Override
    @Transactional
    public void delete(Long id, Long currentUserId) {
        if (currentUserId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 필요합니다");
        }
        var board = boardRepository.findByIdAndDeleteYn(id, "N")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
        if (!board.getUser().getId().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "작성자만 삭제 가능합니다");
        }
        board.softDelete(); // deleteYn = "Y"
    }
}
