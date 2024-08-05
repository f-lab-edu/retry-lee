package com.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class UserResponseDto {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class SignInRes{
        String accessToken;
        String refreshToken;
    }
}
