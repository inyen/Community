package com.example.community.board;

import com.example.community.board.dto.BoardDtos;
import com.example.community.common.SuccessResponse;
import com.example.community.common.auth.LoginUser;
import com.example.community.user.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
/*
REST 엔드포인트, DTO ↔ 엔티티 변환 트리거
 */

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BoardService boardService;

    //게시글 작성
    @PostMapping
    public ResponseEntity<BoardDtos.CreateRes> create(
            @LoginUser Long loginUserId, // 현재 로그인 사용자 id 주입
            @Valid @RequestBody BoardDtos.CreateReq req) {
        //FK 사용자 확인
        //BAD_REQUEST -> 400
        var user = userRepository.findById(loginUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다."));


        //게시글 작성 시 포함할 정보
        var saved = boardRepository.save(
                Board.builder()
                        .user(user)
                        .title(req.title())
                        .content(req.content())
                        .deleteYn("N")
                        .build()
        );
        //응답 DTO 변환, CREATED -> 201
        return ResponseEntity.status(HttpStatus.CREATED).body(BoardDtos.CreateRes.from(saved));
    }

    //게시글 목록(deleteYn = 'N'만 페이징)
    @GetMapping
    public Page<BoardDtos.ListItem> list (
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return boardRepository.findByDeleteYn("N", pageable)
                .map(BoardDtos.ListItem::from);
    }

    //게시글 상세: 회원/비회원 모두 접근
    @GetMapping("/{id}")
    public ResponseEntity<BoardDtos.DetailRes> detail(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.getDetail(id));
    }

    //게시글 수정: 작성자만
    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse> update(
            @PathVariable Long id,
            @LoginUser Long loginUserId,
            @Valid @RequestBody BoardDtos.UpdateReq req
    ){
        boardService.update(id, loginUserId, req); //401, 404, 403 확인
        return ResponseEntity.ok(SuccessResponse.of("게시글이 수정되었습니다."));
    }

    //게시글 삭제: 작성자만
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> delete(
            @PathVariable Long id,
            @LoginUser Long loginUserId
    ){
        boardService.delete(id, loginUserId); //401, 404, 403 확인
        return ResponseEntity.ok(SuccessResponse.of("게시글이 삭제되었습니다."));
    }
}