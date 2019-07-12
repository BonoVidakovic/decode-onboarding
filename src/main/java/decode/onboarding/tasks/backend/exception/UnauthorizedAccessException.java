package decode.onboarding.tasks.backend.exception;

import decode.onboarding.tasks.backend.model.User;

public class UnauthorizedAccessException extends Exception {
    public UnauthorizedAccessException(User user) {
        super("User: " + user.getUsername() + " tried operation with insufficient privilege");
    }
}
