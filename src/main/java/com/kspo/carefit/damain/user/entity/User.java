package com.kspo.carefit.damain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username; // 유저 이름
    private String email; // 유저 이메일
    private String nickname;
    private Role role; // 유저권한
    private Sido sido; // 시도 코드
    private Sigungu sigungu; // 시군구 코드
    private Disability disability; // 장애 코드

    /*
    @OneToMany(mappedBy = "user", CascadeType.ALL , orphanRemoval = true , fetch = FetchType.LAZY)
    private List<UserOauth2Token> userOauth2Token = new ArrayList<>();

    public void addUserOauth2Token(UserOauth2Token userOauth2Token){
        this.userOauth2Token.add(userOauth2Token);
        userOauth2Token.setUser(this);
    }

     */


}
