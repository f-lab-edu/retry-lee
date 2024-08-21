package com.user.e2eTest;

import com.storage.entity.Account;
import com.storage.entity.User;
import com.storage.repository.AccountRepository;
import com.storage.repository.UserRepository;
import com.user.dto.request.UserRequestDto.UserRegisterReq;
import com.user.dto.request.UserRequestDto.UserSignInReq;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("E2eTest")
public class AuthTest extends BaseE2eTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "Testtest11!!";
    private static final String NICKNAME = "yogurt";

    @BeforeEach
    void setUp() {
        // Create Account Entity
        Account account = Account.builder()
                .email(EMAIL)
                .password(passwordEncoder.encode(PASSWORD))
                .build();
        accountRepository.save(account);

        // Create User Entity
        User user = User.builder()
                .account(account)
                .nickname(NICKNAME)
                .grade("Silver")
                .build();
        userRepository.save(user);
    }

    @AfterEach
    void cleanUpDatabase() {
        userRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("Successfully register an account")
    public void registerAccount() {
        // Create account registration request data
        UserRegisterReq request = new UserRegisterReq("testSuccess@test.com", "Testtest11!!","yogurt", false);

        // Send POST request
        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "/auth/signUp",
                request,
                String.class
        );
        // Verify response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("Fail to register an account with duplicate email")
    public void failRegisterAccountWithDuplicateEmail() {
        // Create account registration request data with existing email
        UserRegisterReq request = new UserRegisterReq("test@test.com", "Testtest11!!", "yogurt", false);

        // Send POST request
        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "/auth/signUp",
                request,
                String.class
        );

        // Verify response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("BE1001");
    }

    @Test
    @DisplayName("Fail to register an account with invalid email format")
    public void invalidEmailFormat() {
        // Create account registration request data with existing email
        UserRegisterReq request = new UserRegisterReq("invalidemail", "Testtest11!!", "yogurt", false);
        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "/auth/signUp",
                request,
                String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("이메일 형식에 맞게 입력하세요");
    }

    @Test
    @DisplayName("Fail to register an account with invalid password format")
    public void invalidPasswordFormat() {
        UserRegisterReq request = new UserRegisterReq("test@test.com", "weak", "yogurt", false);
        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "/auth/signUp",
                request,
                String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("8 ~ 16자로 생성하세요. 대소문자, 특수문자, 숫자를 포함하여야 합니다");
    }

    @Test
    @DisplayName("Fail to register an account with invalid nickname length")
    public void invalidNicknameLength() {
        UserRegisterReq request = new UserRegisterReq("test@test.com", "Testtest11!!", "a", false);
        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "/auth/signUp",
                request,
                String.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("최소 2자, 최대 10자로 생성하세요");
    }

    @Test
    @DisplayName("Success login to an account")
    public void succeedLogin() {
        UserSignInReq request = new UserSignInReq(EMAIL, PASSWORD);

        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "/auth/signIn",
                request,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("accessToken");
        assertThat(response.getBody()).contains("refreshToken");

        // Verify that refreshToken is saved in User entity
        User updatedUser = userRepository.findByEmail(EMAIL).orElseThrow();
        assertThat(updatedUser.getRefreshToken()).isNotNull();
    }

    @Test
    @DisplayName("Fail to login with non-existent email")
    public void loginFailWithNonExistentEmail() {
        UserSignInReq request = new UserSignInReq("nonexist@test.com", PASSWORD);

        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "/auth/signIn",
                request,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("로그인 실패, 이메일이나 비밀번호를 확인해주세요.");
    }

    @Test
    @DisplayName("Fail to login with incorrect password")
    public void loginFailWithIncorrectPassword() {
        UserSignInReq request = new UserSignInReq(EMAIL, "WrongPassword11!");

        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "/auth/signIn",
                request,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("로그인 실패, 이메일이나 비밀번호를 확인해주세요.");
    }

    @Test
    @DisplayName("Fail to login with invalid email format")
    public void loginFailWithInvalidEmailFormat() {
        UserSignInReq request = new UserSignInReq("invalidemail", PASSWORD);

        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "/auth/signIn",
                request,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("이메일 형식에 맞게 입력하세요.");
    }

    @Test
    @DisplayName("Fail to login with invalid password format")
    public void loginFailWithInvalidPasswordFormat() {
        UserSignInReq request = new UserSignInReq(EMAIL, "weak");

        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "/auth/signIn",
                request,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("8 ~ 16자로 입력하세요.");
    }

}
