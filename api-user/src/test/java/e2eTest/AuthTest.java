package e2eTest;

import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

@Tag("E2eTest")
public class AuthTest extends BaseE2eTest {

    @Autowired
    private TestRestTemplate testRestTemplate;
}
