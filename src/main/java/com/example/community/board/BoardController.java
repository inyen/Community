package com.example.community.board;

import com.example.community.board.dto.BoardDtos;
import com.example.community.user.User;
import com.example.community.user.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    //게시글 작성
    @PostMapping
    public ResponseEntity<BoardDtos.CreateRes> create(@Valid @RequestBody BoardDtos.CreateReq req) {
        //FK 확인
        User writer = userRepository.findById(req.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다."));

        Board saved = boardRepository.save(
                Board.builder()
                        .user(writer)
                        .title(req.title())
                        .content(req.content())
                        .deleteYn("N")
                        .build()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(BoardDtos.CreateRes.from(saved));
    }

    //게시글 목록(deleteYn = 'N'만 페이징)
    @GetMapping
    public Page<BoardDtos.ListItem> list (
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return boardRepository.findByDeleteYn("N", pageable)
                .map(BoardDtos.ListItem::from);
    }
}
