package com.example.community.board;

import com.example.community.common.BaseEntity;
import com.example.community.user.User;
import jakarta.persistence.*;
import lombok.*;
/*
JPA Entity
 */
@Entity
@Table(name = "BOARD")
@Getter @Setter
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@AllArgsConstructor (access = AccessLevel.PRIVATE)
@Builder
public class Board extends BaseEntity {

    @Id //엔티티의 PK -> 영속성 관리
    @GeneratedValue(strategy = GenerationType.IDENTITY) //DB가 AUTO_INCREMENT로 PK를 생성하고 JPA가 값을 받아옴
    private Long id;

    //작성자(FK: board.user_id -> user.id)
    //성능(지연로딩: 게시글만 먼저 작성하고 작성자 정보가 필요한 순간에만 쿼리) + FK NotNull 보장
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    //실제 FK 컬럼명(user_id)과 외래키 제약 이름 명확히 연결
    @JoinColumn(
            name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_board_user")
    )
    private User user;

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
        this.title = newTitle;
        this.content = newContent;
    }
}