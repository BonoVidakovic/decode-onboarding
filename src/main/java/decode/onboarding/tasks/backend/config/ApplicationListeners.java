package decode.onboarding.tasks.backend.config;

import decode.onboarding.tasks.backend.config.events.UsersInitializedEvent;
import decode.onboarding.tasks.backend.model.Event;
import decode.onboarding.tasks.backend.model.User;
import decode.onboarding.tasks.backend.repository.EventRepository;
import decode.onboarding.tasks.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@Component
@AllArgsConstructor
public class ApplicationListeners {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final PasswordEncoder passwordEncoder;
    private final String username = "bvidakovic";

    @EventListener
    public void initializeUsers(ContextStartedEvent contextStartedEvent) {
        userRepository.save(User.builder()
                .username(username)
                .password(passwordEncoder.encode("password"))
                .build());
    }

    @EventListener
    public void initializeEvents(UsersInitializedEvent usersInitializedEvent) {
        IntStream.range(-15, 15)
                .mapToObj(this::generateEventFromSeed)
                .forEach(eventRepository::save);
    }

    private Event generateEventFromSeed(Integer seed) {
        User user = userRepository.getByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return Event.builder()
                .description("Event " + seed)
                .startTime(LocalDateTime.now().minusDays(seed))
                .endTime(LocalDateTime.now().minusDays(seed).plusHours(1))
                .owner(user)
                .build();
    }
}
