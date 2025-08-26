package com.example.community.board;

import com.example.community.board.dto.BoardDtos;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
/*
목록/상세조회, 수정/삭제 도메인 규칙, 권한 확인
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) //읽기 성능 최적화, 쓰기 메서드는 별도
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Override
    public BoardDtos.DetailRes getDetail(Long id) {
        var board = boardRepository.findByIdAndDeleteYn(id, "N")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));
        return BoardDtos.DetailRes.from(board);
    }

    //401(비로그인) -> 404(없음/삭제됨) -> 403(권한) 순으로 확인
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
        board.applyUpdate(req.title(), req.content()); //도메인 메서드로 변경, Entity 메서드 호출
        //트랜잭션 끝날 때 JPA가 변경감지(Dirty Checking)로 UPDATE 퀴리 실행
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
        //트랜잭션 끝날 때 JPA가 변경감지(Dirty Checking)로 UPDATE 퀴리 실행
    }
}