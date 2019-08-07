package decode.onboarding.tasks.backend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import decode.onboarding.tasks.backend.BackendApplication;
import decode.onboarding.tasks.backend.model.Event;
import decode.onboarding.tasks.backend.model.User;
import decode.onboarding.tasks.backend.repository.EventRepository;
import decode.onboarding.tasks.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

//Pitati zašto ne radi kada je pokrenuto s drugim testovima (vjv kontext i veza na bazu jebu)

@SpringBootTest(classes = {BackendApplication.class, InitializeDatabase.class})
@AutoConfigureMockMvc
@PropertySources({
        @PropertySource("classpath:application-dev.properties"),
        @PropertySource("classpath:application.properties")
})
class EventsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @WithUserDetails("bvidakovic")
    void getEventsForIntervalTest() throws Exception {

        List<Event> events = IntStream.range(0, 20)
                .mapToObj(this::generateEventFromSeed)
                .map(eventRepository::save)
                .collect(Collectors.toList());

        mockMvc.perform(get("/events")
                .param("from", events.get(5).getStartTime().toString())
                .param("to", events.get(10).getEndTime().toString()))
                .andDo(print())
                .andExpect(content().string(objectMapper.writeValueAsString(events.subList(5, 10))));
    }

    private Event generateEventFromSeed(Integer seed) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return Event.builder()
                .description("Event " + seed)
                .startTime(LocalDateTime.now().plusDays(seed))
                .endTime(LocalDateTime.now().plusDays(seed).plusHours(1))
                .owner(user)
                .build();
    }

}
