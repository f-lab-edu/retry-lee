package com.user.utils.jwt;

import com.user.utils.enums.TokenType;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.access-token.expire}") Duration accessExpire,
                            @Value("${jwt.refresh-token.expire}") Duration refreshExpire){
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        TokenType.ACCESS.setExpiredMs(accessExpire.toMillis());
        TokenType.REFRESH.setExpiredMs(refreshExpire.toMillis());
    }

    public <T> T getClaim(String token, String claimName, Class<T> requiredType) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get(claimName, requiredType);
    }

    public String generateToken(TokenType tokenType, Long userId, Date currentDate){
        return Jwts.builder()
                .subject(String.valueOf(tokenType))
                .claim("userId", userId)
                .issuedAt(currentDate)
                .expiration(new Date(currentDate.getTime() + tokenType.getExpiredMs()))
                .signWith(secretKey)
                .compact();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(6).trim();
        }
        return "";
    }
}
