package com.user.utils.jwt;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.Objects;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final Long accessTokenExpire;
    private final Long refreshTokenExpire;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.access-token.expire}") Duration accessExpire,
                            @Value("${jwt.refresh-token.expire}") Duration refreshExpire){
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.accessTokenExpire = accessExpire.toMillis();
        this.refreshTokenExpire = refreshExpire.toMillis();
    }

    public Long getUserId(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("userId", Long.class);
    }

    public String getEmail(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("email", String.class);
    }

    public Boolean isTokenExpired(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    public String generateToken(String tokenType, Long userId, Date currentDate){
        Long expiredMs = 0L;
        if(Objects.equals(tokenType, "Access")) {
             expiredMs = accessTokenExpire;
        }
        else if(Objects.equals(tokenType, "Refresh")) {
            expiredMs = refreshTokenExpire;
        }
        return Jwts.builder()
                .subject(tokenType)
                .claim("userId", userId)
                .issuedAt(currentDate)
                .expiration(new Date(currentDate.getTime() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(6).trim();
        }
        return null;
    }
}
