package com.user.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserRequestDto {

    @Getter
    @AllArgsConstructor
    public static class UserRegisterReq {
        @NotBlank
        @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "이메일 형식에 맞게 입력하세요.")
        private String email;

        @NotBlank
        @Pattern(regexp = "(?=.*[0-9])(?=.*[A-Za-z])(?=.*\\W)(?=\\S+$).{8,16}", message = "8 ~ 16자로 생성하세요. 대소문자, 특수문자, 숫자를 포함하여야 합니다.")
        private String password;

        @NotBlank
        @Size(min = 2, max = 10, message = "최소 2자, 최대 10자로 생성하세요")
        private String nickname;
    }

    @Getter
    @AllArgsConstructor
    public static class UserSignInReq{
        @NotBlank
        private String email;
        @NotBlank
        private String password;
    }
}
