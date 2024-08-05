package com.user.service;

import com.storage.entity.Account;
import com.storage.entity.User;
import com.storage.repository.AccountRepository;
import com.storage.repository.UserRepository;
import com.user.dto.request.TokenRequestDto;
import com.user.dto.request.UserRequestDto;
import com.user.dto.request.UserRequestDto.UserRegisterReq;
import com.user.dto.response.UserResponseDto.SignInRes;
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

    @Transactional
    public void register(UserRegisterReq request) {
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("이메일 중복 (이메일을 확인해주세요): " + request.getEmail());
        }

        // Account 엔티티 생성
        Account account = Account.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        // User 엔티티 생성
        User user = User.builder()
                .nickname(request.getNickname())
                .account(account)
                .grade("silver")
                .build();

        // 저장
        accountRepository.save(account);
        userRepository.save(user);
    }

    /**
     * TODO
     * Custom Exception 생성 후 수정
     * @param req
     * @return
     */
    @Transactional
    public SignInRes signIn(UserRequestDto.UserSignInReq req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("유효한 이메일이 아닙니다."));

        if(!passwordEncoder.matches(req.getPassword(), user.getAccount().getPassword())){
            throw new RuntimeException("비밀번호 확인");
        }
        // 토큰 생성
        String accessToken = jwtTokenProvider.generateToken("Access", user.getUserId(), new Date());
        String refreshToken;

        // Refresh Token 확인
        if (user.getRefreshToken() == null || jwtTokenProvider.isTokenExpired(user.getRefreshToken())) {
            refreshToken = jwtTokenProvider.generateToken("Refresh", user.getUserId(), new Date());
            user.setRefreshToken(refreshToken);
        } else {
            refreshToken = user.getRefreshToken();
        }

        return SignInRes.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * 요청시 accessToken 재발행
     * @param req
     * @return
     */
    public String reissueToken(TokenRequestDto req) {
        Long userId = jwtTokenProvider.getUserId(req.getRefreshToken());
        User user = userRepository.findByUserIdAndRefreshToken(userId, req.getRefreshToken())
                .orElseThrow(() -> new RuntimeException(""));// TODO custom exception 추가

        return jwtTokenProvider.generateToken("Access", user.getUserId(), new Date());
    }
}
