package decode.onboarding.tasks.backend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import decode.onboarding.tasks.backend.model.Event;
import decode.onboarding.tasks.backend.model.User;
import decode.onboarding.tasks.backend.repository.EventRepository;
import decode.onboarding.tasks.backend.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@PropertySource("classpath:application-dev.properties")
public class EventsIntegrationTest {

    private final User currentUser;
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WebApplicationContext webApplicationContext;

    public EventsIntegrationTest() {
        this.currentUser = User.builder()
                .username("username")
                .password(new BCryptPasswordEncoder().encode("password"))
                .build();
    }

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getEventsForIntervalTest() throws Exception {

        userRepository.save(currentUser);
        SecurityContextHolder.getContext().setAuthentication(new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return currentUser.asUserDetails().getAuthorities();
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return currentUser.asUserDetails();
            }

            @Override
            public Object getPrincipal() {
                return currentUser.getUsername();
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
            }

            @Override
            public String getName() {
                return currentUser.getUsername();
            }
        });

        List<Event> events = IntStream.range(-15, 15)
                .mapToObj(this::generateEventFromSeed)
                .collect(Collectors.toList());


        events.forEach(eventRepository::save);

        mockMvc.perform(get("events")
                .param("from", events.get(5).getStartTime().toString())
                .param("to", events.get(10).getEndTime().toString()))
                .andDo(print())
                .andExpect(content().string(objectMapper.writeValueAsString(events.subList(5, 10))));
    }

    private Event generateEventFromSeed(Integer seed) {
        return Event.builder()
                .description("Event " + seed)
                .startTime(LocalDateTime.now().plusMinutes(seed))
                .endTime(LocalDateTime.now().plusMinutes(seed + 5))
                .build();
    }

}
