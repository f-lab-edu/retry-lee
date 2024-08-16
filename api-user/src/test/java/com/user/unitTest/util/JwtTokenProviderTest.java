package com.user.unitTest.util;

import com.user.utils.enums.TokenType;
import com.user.utils.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.mock.web.MockHttpServletRequest;
import java.time.Duration;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

@Tag("UnitTest")
public class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private final String secret = "testSecretKeyWithAtLeast32Characters";
    private final Duration accessExpire = Duration.ofMinutes(30);
    private final Duration refreshExpire = Duration.ofDays(14);

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(secret, accessExpire, refreshExpire);
    }

    @Test
    @DisplayName("Successfully generate access token")
    void generateAccessTokenSuccess() {
        Long userId = 1L;
        String token = jwtTokenProvider.generateToken(TokenType.ACCESS, userId, new Date());

        assertNotNull(token);
        assertTrue(jwtTokenProvider.validateToken(token));
        assertEquals(userId, jwtTokenProvider.getClaim(token, "userId", Long.class));
        assertEquals("ACCESS", jwtTokenProvider.getClaim(token, "sub", String.class));
    }

    @Test
    @DisplayName("Successfully validate token")
    void validateTokenSuccess() {
        String token = jwtTokenProvider.generateToken(TokenType.ACCESS, 1L, new Date());
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    @DisplayName("Fail to validate expired token")
    void failValidateByExpiredToken() {
        Long userId = 1L;
        Date pastDate = new Date(System.currentTimeMillis() - 1000 * 60 * 60); // 1시간 전
        String token = jwtTokenProvider.generateToken(TokenType.ACCESS, userId, pastDate);

        assertFalse(jwtTokenProvider.validateToken(token));
    }

    @Test
    @DisplayName("Fail to validate token with invalid signature")
    void failValidateTokenByInvalidSignature() {
        String invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjEsInN1YiI6IkFDQ0VTUyIsImlhdCI6MTYxNjIzOTAyMn0.invalid_signature";
        assertFalse(jwtTokenProvider.validateToken(invalidToken));
    }

    @Test
    @DisplayName("Successfully resolve token from HTTP request")
    void resolveTokenFromRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String token = "validToken";
        request.addHeader("Authorization", "Bearer " + token);

        String resolvedToken = jwtTokenProvider.resolveToken(request);
        assertEquals(token, resolvedToken);
    }

    @Test
    @DisplayName("Fail to resolve token from request without Bearer prefix")
    void failResolveTokenByNoBearerPrefix() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "validToken");

        String resolvedToken = jwtTokenProvider.resolveToken(request);
        assertEquals("", resolvedToken);
    }

    @Test
    @DisplayName("Successfully get claim from token")
    void getClaimFromToken() {
        Long userId = 1L;
        String token = jwtTokenProvider.generateToken(TokenType.ACCESS, userId, new Date());

        Long extractedUserId = jwtTokenProvider.getClaim(token, "userId", Long.class);
        String tokenType = jwtTokenProvider.getClaim(token, "sub", String.class);

        assertEquals(userId, extractedUserId);
        assertEquals("ACCESS", tokenType);
    }

    @Test
    @DisplayName("Fail to get non-existent claim from token")
    void failGetClaimFromTokenByNonExistentClaim() {
        String token = jwtTokenProvider.generateToken(TokenType.ACCESS, 1L, new Date());

        assertNull(jwtTokenProvider.getClaim(token, "nonExistentClaim", String.class));
    }
}
