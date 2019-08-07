package decode.onboarding.tasks.backend.controller;

import decode.onboarding.tasks.backend.exception.UnauthorizedAccessException;
import decode.onboarding.tasks.backend.model.Event;
import decode.onboarding.tasks.backend.model.EventStatistics;
import decode.onboarding.tasks.backend.service.EventService;
import decode.onboarding.tasks.backend.service.EventStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("events")
@RequiredArgsConstructor
@Slf4j
public class EventsController {

    private final EventService eventService;
    private final EventStatisticsService eventStatisticsService;

    @PostMapping("")
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
    public List<Event> getEventsForInterval(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        log.info("Requested events from: " + from + " to: " + to);
        return eventService.getEventsForInterval(from, to);
    }

    /*
     * Needlessly async, just playing with non-blocking
     */
    @GetMapping("statistics")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    public DeferredResult<ResponseEntity<EventStatistics>> getEventStatistics(@RequestParam LocalDateTime from,
                                                                              @RequestParam LocalDateTime to) {
        DeferredResult<ResponseEntity<EventStatistics>> output = new DeferredResult<>();
        Thread uselessThread = new Thread(() -> output.setResult(new ResponseEntity<>(eventStatisticsService.getEventsStatistics(from, to), HttpStatus.OK)));
        uselessThread.start();
        return output;
    }
}
