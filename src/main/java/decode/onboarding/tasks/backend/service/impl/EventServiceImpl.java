package decode.onboarding.tasks.backend.service.impl;

import decode.onboarding.tasks.backend.exception.UnauthorizedAccessException;
import decode.onboarding.tasks.backend.model.Event;
import decode.onboarding.tasks.backend.model.User;
import decode.onboarding.tasks.backend.repository.EventRepository;
import decode.onboarding.tasks.backend.repository.UserRepository;
import decode.onboarding.tasks.backend.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public Event saveEvent(Event event) {

        event.setOwner(getCurrentUser());

        return eventRepository.save(event);
    }

    @Override
    public boolean deleteEvent(Long id) throws UnauthorizedAccessException {

        Event event = eventRepository.getOne(id);
        if (event == null) {
            return false;
        }

        User currentUser = getCurrentUser();

        if (!event.getOwner().equals(currentUser)) {
            throw new UnauthorizedAccessException(currentUser);
        }

        eventRepository.deleteById(id);
        return true;
    }

    @Override
    public List<Event> getEventsForInterval(LocalDateTime from, LocalDateTime to) {
        return eventRepository.findAll()
                .stream()
                .filter(event -> event.getOwner().equals(getCurrentUser()))
                .filter(event -> event.getStartTime().isAfter(from))
                .filter(event -> event.getEndTime().isBefore(to))
                .collect(Collectors.toList());
    }

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return userRepository.getByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't fetch authenticated user: " + username + " from database"));
    }
}
