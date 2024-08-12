package e2eTest;

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
public class BaseE2eTest {
    @Container
    private static MySQLContainer<?> MySQLContainer = new MySQLContainer<>("mysql:8.0");

    @LocalServerPort
    int port;

    @Bean
    public TestRestTemplate testRestTemplate() {
        RestTemplateBuilder builder = new RestTemplateBuilder()
                .rootUri("http://localhost:" + port);
        return new TestRestTemplate(builder);
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.driver-class-name", MySQLContainer::getDriverClassName);
        registry.add("spring.datasource.jdbc-url", MySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", MySQLContainer::getUsername);
        registry.add("spring.datasource.password", MySQLContainer::getPassword);
    }
}