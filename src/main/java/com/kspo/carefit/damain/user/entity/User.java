package com.kspo.carefit.damain.user.entity;

import com.kspo.carefit.base.security.oauth2.entity.UserOauth2Token;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    private String nickname; // 유저 닉네임
    private String role; // 유저권한
    private Integer sidoCode; // 시도 코드
    private Integer sigunguCode; // 시군구 코드
    private Integer disabilityCode; // 장애 코드

    @Builder.Default
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private List<UserOauth2Token> userOauth2Token = new ArrayList<>();

    public void addUserOauth2Token(UserOauth2Token userOauth2Token){
        this.userOauth2Token.add(userOauth2Token);
        userOauth2Token.setUser(this);
    }

    public void updateUser(String username,
                           String email,
                           String nickname){
        this.username = username;
        this.email = email;
        this.nickname = nickname;
    }

    public void updateCodes(Integer sidoCode,
                            Integer sigunguCode,
                            Integer disabilityCode){
        this.sidoCode = sidoCode;
        this.sigunguCode = sigunguCode;
        this.disabilityCode = disabilityCode;
    }


}
