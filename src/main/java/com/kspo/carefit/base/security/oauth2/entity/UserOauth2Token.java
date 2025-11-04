package com.kspo.carefit.base.security.oauth2.entity;

import com.kspo.carefit.damain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Table(
        name = "user_oauth2token",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "provider"})
)
public class UserOauth2Token {

    @Id
    private Long id;

    private String provider;
    private String accessToken;
    private String refreshToken;
    private Instant accessTokenExpiresAt;
    private Instant refreshTokenExpiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    public void updateTokens(String refreshToken,
                             Instant accessExpiresAt,
                             Instant refreshExpiresAt) {

        this.refreshToken = refreshToken;
        this.accessTokenExpiresAt = accessExpiresAt;
        this.refreshTokenExpiresAt = refreshExpiresAt;
    }

}
