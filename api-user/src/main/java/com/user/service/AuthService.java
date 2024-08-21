package com.user.service;

import com.storage.entity.Account;
import com.storage.entity.Admin;
import com.storage.entity.User;
import com.storage.repository.AccountRepository;
import com.storage.repository.AdminRepository;
import com.storage.repository.UserRepository;
import com.user.exception.CustomException;
import com.user.exception.type.ErrorCode;
import com.user.dto.request.TokenRequestDto;
import com.user.dto.request.UserRequestDto;
import com.user.dto.request.UserRequestDto.UserRegisterReq;
import com.user.dto.response.TokenResponseDto;
import com.user.dto.response.UserResponseDto.SignInRes;
import com.user.enums.TokenType;
import com.user.enums.UserType;
import com.user.utils.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AdminRepository adminRepository;

    @Transactional
    public void register(UserRegisterReq request) {
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.ERROR_BE1001);
        }

        // Account entity
        Account account = Account.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        // Account save
        accountRepository.save(account);

        if (request.isAdmin()) {
            // Admin entity
            Admin admin = Admin.builder()
                    .nickname(request.getNickname())
                    .account(account)
                    .build();
            adminRepository.save(admin);
        } else {
            // User entity
            User user = User.builder()
                    .nickname(request.getNickname())
                    .account(account)
                    .grade("silver")
                    .build();
            userRepository.save(user);
        }
    }

    /**
     * TODO
     * Custom Exception 생성 후 수정
     * @param req
     * @return
     */
    @Transactional
    public SignInRes signIn(UserRequestDto.UserSignInReq req) {
        Account account = accountRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_BE1003));

        if (!passwordEncoder.matches(req.getPassword(), account.getPassword())) {
            throw new CustomException(ErrorCode.ERROR_BE1003);
        }

        UserType userType;
        Long id;

        if (adminRepository.existsByAccountId(account.getAccountId())) {
            Admin admin = adminRepository.findByAccountId(account.getAccountId())
                    .orElseThrow(() -> new CustomException(ErrorCode.ERROR_BE1003));
            userType = UserType.ADMIN;
            id = admin.getAdminId();
        } else {
            User user = userRepository.findByAccountId(account.getAccountId())
                    .orElseThrow(() -> new CustomException(ErrorCode.ERROR_BE1003));
            userType = UserType.USER;
            id = user.getUserId();
        }

        String accessToken = jwtTokenProvider.generateToken(TokenType.ACCESS, userType, id, new Date());
        String refreshToken = jwtTokenProvider.generateToken(TokenType.REFRESH, userType, id, new Date());

        // Refresh 토큰 저장
        if (userType == UserType.ADMIN) {
            adminRepository.findById(id).ifPresent(admin -> admin.setRefreshToken(refreshToken));
        } else {
            userRepository.findById(id).ifPresent(user -> user.setRefreshToken(refreshToken));
        }

        return SignInRes.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userType(userType.name())
                .build();
    }

    /**
     * 요청시 accessToken 재발행
     * @param req
     * @return
     */
    @Transactional
    public TokenResponseDto getAccessTokenByRefreshToken(TokenRequestDto req) {
        if (!jwtTokenProvider.validateToken(req.getRefreshToken())) {
            throw new CustomException(ErrorCode.ERROR_BE1005);
        }

        Long id = jwtTokenProvider.getClaim(req.getRefreshToken(), "id", Long.class);
        UserType userType = jwtTokenProvider.getClaim(req.getRefreshToken(), "userType", UserType.class);

        String newAccessToken;
        String newRefreshToken;

        if (userType == UserType.ADMIN) {
            Admin admin = adminRepository.findByAdminIdAndRefreshToken(id, req.getRefreshToken())
                    .orElseThrow(() -> new CustomException(ErrorCode.ERROR_BE1004));
            newAccessToken = jwtTokenProvider.generateToken(TokenType.ACCESS, userType, id, new Date());
            newRefreshToken = jwtTokenProvider.generateToken(TokenType.REFRESH, userType, id, new Date());
            admin.setRefreshToken(newRefreshToken);
        } else {
            User user = userRepository.findByUserIdAndRefreshToken(id, req.getRefreshToken())
                    .orElseThrow(() -> new CustomException(ErrorCode.ERROR_BE1004));
            newAccessToken = jwtTokenProvider.generateToken(TokenType.ACCESS, userType, id, new Date());
            newRefreshToken = jwtTokenProvider.generateToken(TokenType.REFRESH, userType, id, new Date());
            user.setRefreshToken(newRefreshToken);
        }

        return TokenResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
}
