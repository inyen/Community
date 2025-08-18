package com.example.community.board;

import com.example.community.common.BaseEntity;
import com.example.community.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "BOARD")
@Getter @Setter
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@AllArgsConstructor (access = AccessLevel.PRIVATE)
@Builder
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //FK: board.user_id -> user.id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_board_user")
    )
    private User user;
  /*  @Column(name = "author_id", nullable = false)
    private Long authorId;*/

    @Column(name = "TITLE", nullable = false, length = 150)
    private String title;

    @Lob
    @Column(name = "CONTENT", nullable = false, length = 1500)
    private String content;

    @Column(name = "DELETE_YN", nullable = false, length = 1)
    @Builder.Default
    private String deleteYn = "N";

    public void softDelete() { this.deleteYn = "Y"; }

    public void applyUpdate(String newTitle, String newContent){
        if (newTitle != null && !newTitle.isBlank()) this.title = newTitle;
        if (newContent != null && !newContent.isBlank()) this.content = newContent;
    }
}
