package com.example.community.user;

import com.example.community.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
/*
사용자 테이블 매핑, 도메인 메서드 보유
 */
@Entity
@Table(name = "USER",
        //유니크
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_user_id", columnNames = "user_id"),
                @UniqueConstraint(name = "uk_user_user_name", columnNames = "user_name")
        })

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    //PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID", nullable = false, length = 8) //로그인 아이디
    private String userId;

    @Column(name = "PASSWORD", nullable = false, length = 100)  //BCrypt 해시 길이 100자로 고정
    private String password;

    @Column(name = "USER_NAME", nullable = false, length = 24)  //닉네임
    private String userName;

    @Column(name = "DELETE_YN", nullable = false, length = 1)
    private String deleteYn = "N";

    //도메인 메서드
    public void softDelete() { this.deleteYn = "Y"; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setPassword(String password) { this.password = password; }


    public static User create(String userId, String password, String userName) {
        return User.builder()
                .userId(userId)
                .password(password)
                .userName(userName)
                .deleteYn("N")
                .build();
    }
}
