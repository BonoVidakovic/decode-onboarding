package decode.onboarding.tasks.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootTest
@PropertySources({
        @PropertySource("classpath:application-dev.properties"),
        @PropertySource("classpath:application.properties")
})
class BackendApplicationTests {

    @Test
    void contextLoads() {
    }

}
