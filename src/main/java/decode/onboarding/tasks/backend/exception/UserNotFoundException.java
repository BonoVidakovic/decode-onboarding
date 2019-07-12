package decode.onboarding.tasks.backend.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("Couldn't fetch authenticated user: " + username + " from database");
    }
}
