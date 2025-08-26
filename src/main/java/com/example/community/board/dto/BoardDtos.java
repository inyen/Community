package com.example.community.board.dto;

import com.example.community.board.Board;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
/*
생성/수정/응답 DTO 모음
 */
public class BoardDtos {

    //생성 요청: 우효성 확인
    public record CreateReq(
            @NotBlank @Size(max = 150) String title,
            @NotBlank @Size(max = 1500) String content
    ) { }

    //생성 응답
    public record CreateRes(
            Long id, String title, LocalDateTime createdTime
    ) {
        public static CreateRes from(Board b) {
            return new CreateRes(b.getId(), b.getTitle(), b.getCreatedTime());
        }
    }

    //목록 응답(작성자 표시)
    public record ListItem(
            Long id, String title, String content,
            Long userId, String userName,
            LocalDateTime createdTime
    ){
        public static ListItem from(Board b) {
            return new ListItem(
                    b.getId(),
                    b.getTitle(),
                    b.getContent(),
                    b.getUser().getId(),
                    b.getUser().getUserName(),
                    b.getCreatedTime()
            );
        }
    }

    //상세 응답
    public record DetailRes(
            Long id, String title, String content,
            Long userId, String userName,
            LocalDateTime createdTime,
            LocalDateTime updatedTime
    ){
        public static DetailRes from(Board b) {
            return new DetailRes(
                    b.getId(),
                    b.getTitle(),
                    b.getContent(),
                    b.getUser().getId(),
                    b.getUser().getUserName(),
                    b.getCreatedTime(),
                    b.getUpdatedTime()
            );
        }
    }

    //수정 요청
    public record UpdateReq(
            @NotBlank @Size(max = 150) String title,
            @NotBlank @Size(max = 1500) String content
    ) { }
}