package com.user.controller;

import com.user.dto.request.TokenRequestDto;
import com.user.dto.request.UserRequestDto.UserRegisterReq;
import com.user.dto.request.UserRequestDto.UserSignInReq;
import com.user.dto.response.TokenResponseDto;
import com.user.dto.response.UserResponseDto.SignInRes;
import com.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity signup(@RequestBody @Valid UserRegisterReq req) {
        authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<SignInRes> singIn(@RequestBody @Valid UserSignInReq req) {
        SignInRes signInRes = authService.signIn(req);
        return ResponseEntity.ok(signInRes);
    }

    @PatchMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissueToken(@RequestBody @Valid TokenRequestDto req){
        String accessToken = authService.reissueToken(req);
        return ResponseEntity.ok(new TokenResponseDto(accessToken));
    }
}
