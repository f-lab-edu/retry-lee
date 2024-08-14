package com.user.e2eTest;

import com.storage.entity.Account;
import com.storage.repository.AccountRepository;
import com.user.dto.request.UserRequestDto.UserRegisterReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("E2eTest")
public class AuthTest extends BaseE2eTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    @DisplayName("정상적으로 회원가입에 성공한다.")
    public void accountRegistration() {
        // 회원가입 요청 데이터 생성
        UserRegisterReq request = new UserRegisterReq("testSuccess@test.com", "Testtest11!!","yogurt");

        // POST 요청 전송
        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "http://localhost:" + port + "/auth/signUp",
                request,
                String.class
        );
        // 응답 검증
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("중복된 이메일일 경우 회원가입에 실패한다.")
    public void failedAccountRegistrationWithExistingEmail() {
        // 기존 계정 생성
        Account existingAccount = Account.builder()
                .accountId(0L)
                .email("test@test.com")
                .password("Encoded11!!")
                .build();
        accountRepository.save(existingAccount);

        // 동일한 이메일로 회원가입 요청 데이터 생성
        UserRegisterReq request = new UserRegisterReq("test@test.com", "Testtest11!!", "yogurt");

        // POST 요청 전송
        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "http://localhost:" + port + "/auth/signUp",
                request,
                String.class
        );

        // 응답 검증
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("BE1001");
    }

    @Test
    @DisplayName("이메일의 형식이 올바르지 않은 경우 작성된 메시지가 반환된다.")
    public void invalidEmailFormat() {
        UserRegisterReq request = new UserRegisterReq("invalidemail", "Testtest11!!", "yogurt");
        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "http://localhost:" + port + "/auth/signUp",
                request,
                String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("이메일 형식에 맞게 입력하세요");
    }

    @Test
    @DisplayName("비밀번호의 형식이 올바르지 않은 경우 작성된 메시지가 반환된다.")
    public void invalidPasswordFormat() {
        UserRegisterReq request = new UserRegisterReq("test@test.com", "weak", "yogurt");
        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "http://localhost:" + port + "/auth/signUp",
                request,
                String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("8 ~ 16자로 생성하세요. 대소문자, 특수문자, 숫자를 포함하여야 합니다");
    }

    @Test
    @DisplayName("닉네임의 글자 길이가 범위에 맞지 않는 경우 작성된 메시지가 반환된다.")
    public void invalidNicknameLength() {
        UserRegisterReq request = new UserRegisterReq("test@test.com", "Testtest11!!", "a");
        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "http://localhost:" + port + "/auth/signUp",
                request,
                String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("최소 2자, 최대 10자로 생성하세요");
    }
}
