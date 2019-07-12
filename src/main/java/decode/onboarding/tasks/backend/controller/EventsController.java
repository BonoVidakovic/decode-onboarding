package decode.onboarding.tasks.backend.controller;

import decode.onboarding.tasks.backend.exception.UnauthorizedAccessException;
import decode.onboarding.tasks.backend.model.Event;
import decode.onboarding.tasks.backend.service.EventService;
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

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("events")
public class EventsController {

    private final EventService eventService;

    public EventsController(EventService eventService) {
        this.eventService = eventService;
    }

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
}
