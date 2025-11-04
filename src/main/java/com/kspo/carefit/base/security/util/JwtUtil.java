package com.kspo.carefit.base.security.util;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private SecretKey secretKey;

    public JwtUtil(@Value("${spring.jwt.secret")String secret){

        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts
                        .SIG
                        .HS256
                        .key()
                        .build()
                        .getAlgorithm());

    }

    // 토큰에서 username 추출하는 메소드
    public String getUsername(String token){

        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("username",String.class);
    }

    // 토큰에서 role 추출하는 메소드
    public String getRole(String token){

        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role",String.class);

    }

    // 토큰에서 category 추출하는 메소드
    public String getCategory(String token){

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("category",String.class);
    }

    // 토큰 파기여부 확인 메소드
    public Boolean isExpired(String token){

        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());

    }

    // 토큰 생성메소드
    public String createJwt(String category,
                            String username,
                            String role,
                            Long expiredMs){

        return Jwts.builder()
                .claim("category",category)
                .claim("username",username)
                .claim("role",username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();

    }
}
