package decode.onboarding.tasks.backend.integration;

import decode.onboarding.tasks.backend.config.events.UsersInitializedEvent;
import decode.onboarding.tasks.backend.model.User;
import decode.onboarding.tasks.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@AllArgsConstructor
public class InitializeDatabase {
    private final UserRepository userRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final PasswordEncoder passwordEncoder;

    @EventListener
    public void initializeUsers(ContextRefreshedEvent contextRefreshedEvent) {
        String username = "bvidakovic";
        userRepository.save(User.builder()
                .username(username)
                .password(passwordEncoder.encode("password"))
                .build());
        applicationEventPublisher.publishEvent(new UsersInitializedEvent(this));
    }
}
