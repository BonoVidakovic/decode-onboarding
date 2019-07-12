package decode.onboarding.tasks.backend.controller;

import decode.onboarding.tasks.backend.exception.UnauthorizedAccessException;
import decode.onboarding.tasks.backend.model.Event;
import decode.onboarding.tasks.backend.model.EventStatistics;
import decode.onboarding.tasks.backend.service.EventService;
import decode.onboarding.tasks.backend.service.EventStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("events")
@RequiredArgsConstructor
public class EventsController {

    private final EventService eventService;
    private final EventStatisticsService eventStatisticsService;

    @PostMapping
    public Event createEvent(Event event) {
        event.setID(null);
        return eventService.saveEvent(event);
    }

    @PutMapping("{id}")
    public Event updateEvent(@PathVariable Long id, Event event) {
        event.setID(id);
        return eventService.saveEvent(event);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteEvent(@PathVariable Long id) throws UnauthorizedAccessException {
        if (eventService.deleteEvent(id)) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public List<Event> getEventsForInterval(@RequestParam LocalDateTime from,
                                            @RequestParam LocalDateTime to) {
        return eventService.getEventsForInterval(from, to);
    }

    /*
     * Needlessly async, just playing with non-blocking
     */
    @GetMapping("statistics")
    public DeferredResult<ResponseEntity<EventStatistics>> getEventStatistics(@RequestParam LocalDateTime from,
                                                                              @RequestParam LocalDateTime to) {
        DeferredResult<ResponseEntity<EventStatistics>> output = new DeferredResult<>();
        Thread uselessThread = new Thread(() -> output.setResult(new ResponseEntity<>(eventStatisticsService.getEventsStatistics(from, to), HttpStatus.OK)));
        uselessThread.start();
        return output;
    }
}
