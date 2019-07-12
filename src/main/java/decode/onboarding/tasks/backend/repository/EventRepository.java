package decode.onboarding.tasks.backend.repository;

import decode.onboarding.tasks.backend.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

}
