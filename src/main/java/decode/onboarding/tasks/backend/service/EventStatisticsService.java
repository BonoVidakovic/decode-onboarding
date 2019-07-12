package decode.onboarding.tasks.backend.service;

import decode.onboarding.tasks.backend.model.EventStatistics;

import java.time.LocalDateTime;

public interface EventStatisticsService {
    EventStatistics getEventsStatistics(LocalDateTime from, LocalDateTime to);
}
