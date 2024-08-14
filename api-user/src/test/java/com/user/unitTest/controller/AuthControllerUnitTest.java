package com.user.unitTest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.config.SecurityConfig;
import com.user.controller.AuthController;
import com.user.dto.request.UserRequestDto.UserRegisterReq;
import com.user.exception.CustomException;
import com.user.exception.type.ErrorCode;
import com.user.security.CustomUserDetailService;
import com.user.service.AuthService;
import com.user.utils.jwt.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
@Tag("UnitTest")
public class AuthControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("정상적으로 회원가입에 성공한다.")
    void successSignup() throws Exception {
        UserRegisterReq req = new UserRegisterReq("test@example.com", "Testtest11!!", "yogurt");

        doNothing().when(authService).register(any(UserRegisterReq.class));

        mockMvc.perform(post("/auth/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("중복된 이메일일 경우 회원가입에 실패한다.")
    void signupWithExistingEmail() throws Exception {
        UserRegisterReq req = new UserRegisterReq("existing@example.com", "Password123!", "yogurt");

        doThrow(new CustomException(ErrorCode.ERROR_BE1001)).when(authService).register(any(UserRegisterReq.class));

        mockMvc.perform(post("/auth/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("회원가입 실패(이메일 중복), 이메일을 확인해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("이메일의 형식이 올바르지 않은 경우 작성된 메시지가 반환된다.")
    void signupWithInvalidEmail() throws Exception {
        UserRegisterReq req = new UserRegisterReq("invalidemail", "Password123!", "yogurt");

        mockMvc.perform(post("/auth/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이메일 형식에 맞게 입력하세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("비밀번호의 형식이 올바르지 않은 경우 작성된 메시지가 반환된다.")
     void signupWithInvalidPassword() throws Exception {
        UserRegisterReq req = new UserRegisterReq("test@example.com", "weak", "yogurt");

        mockMvc.perform(post("/auth/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("8 ~ 16자로 생성하세요. 대소문자, 특수문자, 숫자를 포함하여야 합니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("닉네임의 글자 길이가 범위에 맞지 않는 경우 작성된 메시지가 반환된다.")
    void signupWithInvalidNickname() throws Exception {
        UserRegisterReq req = new UserRegisterReq("test@example.com", "Password123!", "a");

        mockMvc.perform(post("/auth/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("최소 2자, 최대 10자로 생성하세요"))
                .andDo(print());
    }
}
