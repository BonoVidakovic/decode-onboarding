package decode.onboarding.tasks.backend.exception;

import decode.onboarding.tasks.backend.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
@Slf4j
public class UnauthorizedAccessException extends Exception {
    public UnauthorizedAccessException(User user) {
        super();
        log.info("User: " + user.getUsername() + " tried operation with insufficient privilege");
    }
}
