package com.user.unitTest.service;

import com.storage.entity.Account;
import com.storage.entity.User;
import com.storage.repository.AccountRepository;
import com.storage.repository.UserRepository;
import com.user.dto.request.UserRequestDto.UserRegisterReq;
import com.user.exception.CustomException;
import com.user.service.AuthService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @InjectMocks
    private AuthService authService;

    @Test
    void testSuccessfulRegistration() {
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
    void testRegistrationWithExistingEmail() {
        // Given
        UserRegisterReq request = new UserRegisterReq("existing@test.com", "Password1!", "testuser");

        // When & Then
        when(accountRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(CustomException.class, () -> authService.register(request));
        verify(accountRepository, never()).save(any(Account.class));
        verify(userRepository, never()).save(any(User.class));
    }
}
