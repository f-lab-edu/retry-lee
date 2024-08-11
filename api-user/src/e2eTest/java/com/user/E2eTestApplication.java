package com.user;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class E2eTestApplication {

    @LocalServerPort
    private int port;

    @Container
    private static MySQLContainer<?> MySQLContainer = new MySQLContainer<>("mysql:8.0");

    @Bean
    public TestRestTemplate testRestTemplate() {
        RestTemplateBuilder builder = new RestTemplateBuilder()
                .rootUri("http://localhost:" + port);
        return new TestRestTemplate(builder);
    }

    /**
     * 동작 순서
     * 1. Spring Test Context Framework -> 테스트 클래스 로드
     * 2. @DynamicPropertySource 가 붙은 메소드 찾음
     * 3. DynamicPropertyRegistry 에 Property 등록
     * 4. Test Context 가 로드될 떄 동적 Property 들이 환경에 추가됨
     * 5. Application Context 가 이 Property 를 사용.
     */
    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.driver-class-name", MySQLContainer::getDriverClassName);
        registry.add("spring.datasource.jdbc-url", MySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", MySQLContainer::getUsername);
        registry.add("spring.datasource.password", MySQLContainer::getPassword);
    }

    /**
     *  - static 을 사용하지 않으면 테스트 메소드마다 컨테이너 인스턴스가 생성됨.
     *  - static 을 사용하여 테스트 컨텍스트가 로드되기 전에 프로퍼티를 설정할 수 있다.
     */
}
