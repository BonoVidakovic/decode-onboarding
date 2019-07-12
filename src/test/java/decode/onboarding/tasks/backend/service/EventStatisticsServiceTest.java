package decode.onboarding.tasks.backend.service;

import decode.onboarding.tasks.backend.model.Event;
import decode.onboarding.tasks.backend.model.EventStatistics;
import decode.onboarding.tasks.backend.service.impl.EventStatisticsServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EventStatisticsServiceTest {

    @Mock
    private EventService eventServiceMock;

    @InjectMocks
    private EventStatisticsServiceImpl eventStatisticsService;

    @Test
    public void testGetEventStatistics() {
        LocalDateTime firstStart = LocalDateTime.of(2019, 1, 1, 0, 0, 0, 0);
        List<Event> events = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            events.add(Event.builder()
                    .startTime(firstStart.plusHours(i))
                    .endTime(firstStart.plusHours(i + 1))
                    .build());
        }

        when(eventServiceMock.getEventsForInterval(firstStart, firstStart.plusDays(3))).thenReturn(events);

        EventStatistics eventStatistics = EventStatistics.builder()
                .averageDuration(60D)
                .count(5L)
                .hourUtilization(5 / 72.)
                .build();

        assertEquals(eventStatistics, eventStatisticsService.getEventsStatistics(firstStart, firstStart.plusDays(3)));
    }
}
