package decode.onboarding.tasks.backend.service.impl;

import decode.onboarding.tasks.backend.model.Event;
import decode.onboarding.tasks.backend.model.EventStatistics;
import decode.onboarding.tasks.backend.service.EventService;
import decode.onboarding.tasks.backend.service.EventStatisticsService;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class EventStatisticsServiceImpl implements EventStatisticsService {

    private final EventService eventService;

    public EventStatisticsServiceImpl(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public EventStatistics getEventsStatistics(LocalDateTime from, LocalDateTime to) {

        EventStatisticsHelper statisticsHelper = new EventStatisticsHelper();
        eventService.getEventsForInterval(from, to)
                .forEach(statisticsHelper::logEvent);

        Duration periodDuration = Duration.between(from, to);

        return EventStatistics.builder()
                .averageDuration(statisticsHelper.getTotalDuration().getSeconds() * 60D / statisticsHelper.getEventsCount())
                .count(statisticsHelper.getEventsCount())
                .hourUtilization((double) (statisticsHelper.getTotalDuration().getSeconds() / periodDuration.getSeconds()))
                .build();
    }

    @Data
    private class EventStatisticsHelper {
        private Duration totalDuration;
        private Long eventsCount;

        EventStatisticsHelper() {
            this.totalDuration = Duration.ofMinutes(0);
            this.eventsCount = 0L;
        }

        void logEvent(Event event) {
            totalDuration = totalDuration.plus(getEventDuration(event));
            eventsCount++;
        }

        private Duration getEventDuration(Event event) {
            return Duration.between(event.getStartTime(), event.getEndTime());
        }

    }
}
