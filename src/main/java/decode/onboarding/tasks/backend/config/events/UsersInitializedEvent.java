package decode.onboarding.tasks.backend.config.events;

import org.springframework.context.ApplicationEvent;

public class UsersInitializedEvent extends ApplicationEvent {

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public UsersInitializedEvent(Object source) {
        super(source);
    }
}
