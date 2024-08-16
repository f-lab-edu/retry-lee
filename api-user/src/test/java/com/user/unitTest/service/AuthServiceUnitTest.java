package com.user.unitTest.service;

import com.storage.entity.Account;
import com.storage.entity.User;
import com.storage.repository.AccountRepository;
import com.storage.repository.UserRepository;
import com.user.dto.request.TokenRequestDto;
import com.user.dto.request.UserRequestDto.UserRegisterReq;
import com.user.dto.request.UserRequestDto.UserSignInReq;
import com.user.dto.response.TokenResponseDto;
import com.user.dto.response.UserResponseDto.SignInRes;
import com.user.exception.CustomException;
import com.user.service.AuthService;
import com.user.utils.enums.TokenType;
import com.user.utils.jwt.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("UnitTest")
public class AuthServiceUnitTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Successfully register an account")
    void successRegister() {
        // Given
        UserRegisterReq request = new UserRegisterReq("test@test.com", "Password1!", "testuser");

        // When
        when(accountRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG");
        authService.register(request);

        // Then
        verify(accountRepository).save(argThat(account ->
                account.getEmail().equals(request.getEmail()) &&
                        account.getPassword().equals("$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG")
        ));
        verify(userRepository).save(argThat(user ->
                user.getNickname().equals(request.getNickname()) &&
                        user.getGrade().equals("silver")
        ));
    }

    @Test
    @DisplayName("Fail to register an account with duplicate email")
    void registerWithExistingEmail() {
        // Given
        UserRegisterReq request = new UserRegisterReq("existing@test.com", "Password1!", "testuser");

        // When & Then
        when(accountRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(CustomException.class, () -> authService.register(request));
        verify(accountRepository, never()).save(any(Account.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Successfully sign in")
    void signInSuccess() {
        // Given
        UserSignInReq req = new UserSignInReq("test@email.com", "Password1!");
        Account account = Account.builder()
                .accountId(1L)
                .email("test@email.com")
                .password("$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG")
                .build();
        User user = User.builder()
                .userId(1L)
                .account(account)
                .nickname("yogurt")
                .grade("Silver")
                .build();

        // When
        when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(req.getPassword(), user.getAccount().getPassword())).thenReturn(true);
        when(jwtTokenProvider.generateToken(eq(TokenType.ACCESS), anyLong(), any(Date.class))).thenReturn("accessToken");
        when(jwtTokenProvider.generateToken(eq(TokenType.REFRESH), anyLong(), any(Date.class))).thenReturn("refreshToken");

        SignInRes result = authService.signIn(req);

        // Then
        assertNotNull(result);
        assertEquals("accessToken", result.getAccessToken());
        assertEquals("refreshToken", result.getRefreshToken());
    }

    @Test
    @DisplayName("Fail to sign in with non-existent user")
    void failSignInWithUserNotFound() {
        // Given
        UserSignInReq req = new UserSignInReq("test@email.com", "Password1!");

        // When & Then
        when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> authService.signIn(req));
    }

    @Test
    @DisplayName("Fail to sign in with incorrect password")
    void failSignInIncorrectPassword() {
        // Given
        UserSignInReq req = new UserSignInReq("test@email.com", "Password1!");
        Account account = Account.builder()
                .accountId(1L)
                .email("test@email.com")
                .password("encodedPassword")
                .build();
        User user = User.builder()
                .userId(1L)
                .account(account)
                .nickname("testUser")
                .grade("NORMAL")
                .build();

        // When & Then
        when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(req.getPassword(), user.getAccount().getPassword())).thenReturn(false);

        assertThrows(CustomException.class, () -> authService.signIn(req));
    }

    @Test
    @DisplayName("Successfully reissue refresh tokens")
    void getAccessTokenByRefreshToken() {
        // Given
        TokenRequestDto req = new TokenRequestDto("validRefreshToken");
        User user = User.builder().userId(1L).refreshToken("validRefreshToken").build();

        // When
        when(jwtTokenProvider.validateToken("validRefreshToken")).thenReturn(true);
        when(jwtTokenProvider.getClaim("validRefreshToken", "userId", Long.class)).thenReturn(1L);
        when(userRepository.findByUserIdAndRefreshToken(1L, "validRefreshToken")).thenReturn(Optional.of(user));
        when(jwtTokenProvider.generateToken(eq(TokenType.ACCESS), eq(1L), any(Date.class))).thenReturn("newAccessToken");
        when(jwtTokenProvider.generateToken(eq(TokenType.REFRESH), eq(1L), any(Date.class))).thenReturn("newRefreshToken");

        TokenResponseDto response = authService.getAccessTokenByRefreshToken(req);

        // Then
        assertNotNull(response);
        assertEquals("newAccessToken", response.getAccessToken());
        assertEquals("newRefreshToken", response.getRefreshToken());
        assertEquals("newRefreshToken", user.getRefreshToken());
    }

    @Test
    @DisplayName("Fail to refresh tokens with expired refresh token")
    void failReissueRefreshTokenWithExpiredToken() {
        // Given
        String expiredRefreshToken = "expiredRefreshToken";
        TokenRequestDto req = new TokenRequestDto(expiredRefreshToken);

        // When & Then
        when(jwtTokenProvider.validateToken(expiredRefreshToken)).thenReturn(false);

        assertThrows(CustomException.class, () -> authService.getAccessTokenByRefreshToken(req));
    }

    @Test
    @DisplayName("Fail to refresh tokens when user not found")
    void failReissueRefreshTokenByUserNotFound() {
        // Given
        String validRefreshToken = "validRefreshToken";
        TokenRequestDto req = new TokenRequestDto(validRefreshToken);

        // When & Then
        when(jwtTokenProvider.validateToken(validRefreshToken)).thenReturn(true);
        when(jwtTokenProvider.getClaim(validRefreshToken, "userId", Long.class)).thenReturn(1L);
        when(userRepository.findByUserIdAndRefreshToken(1L, validRefreshToken)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> authService.getAccessTokenByRefreshToken(req));
    }
}
