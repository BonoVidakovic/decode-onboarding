package decode.onboarding.tasks.backend.service;

import decode.onboarding.tasks.backend.exception.UnauthorizedAccessException;
import decode.onboarding.tasks.backend.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    Event saveEvent(Event event);

    boolean deleteEvent(Long id) throws UnauthorizedAccessException;

    List<Event> getEventsForInterval(LocalDateTime from, LocalDateTime to);
}
