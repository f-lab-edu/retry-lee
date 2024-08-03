package com.user.controller;

import com.user.dto.request.UserDto.UserRegisterReq;
import com.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
