package com.example.community.board.dto;

import com.example.community.board.Board;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class BoardDtos {

    //작성 요청
    public record CreateReq(
            @NotNull Long userId,
            @NotBlank @Size(max = 150) String title,
            @NotBlank @Size(max = 1500) String content
    ) {}

    //작성 응답
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
}
