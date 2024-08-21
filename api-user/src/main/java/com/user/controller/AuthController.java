package com.user.controller;

import com.user.dto.request.TokenRequestDto;
import com.user.dto.request.UserRequestDto.UserRegisterReq;
import com.user.dto.request.UserRequestDto.UserSignInReq;
import com.user.dto.response.TokenResponseDto;
import com.user.dto.response.UserResponseDto.SignInRes;
import com.user.security.CustomUserDetails;
import com.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signUp")
    public ResponseEntity signup(@RequestBody @Valid UserRegisterReq req) {
        authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/signIn")
    public ResponseEntity<SignInRes> signIn(@RequestBody @Valid UserSignInReq req) {
        SignInRes signInRes = authService.signIn(req);
        return ResponseEntity.ok(signInRes);
    }

    @PostMapping("/reissueToken")
    public ResponseEntity<TokenResponseDto> reissueToken(@RequestBody @Valid TokenRequestDto req){
        TokenResponseDto res = authService.getAccessTokenByRefreshToken(req);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("email", userDetails.getUsername());
            response.put("authorities", userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
    }
}
