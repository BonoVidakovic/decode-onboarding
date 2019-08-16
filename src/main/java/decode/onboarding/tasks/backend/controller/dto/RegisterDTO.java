package decode.onboarding.tasks.backend.controller.dto;

import javax.validation.constraints.NotBlank;

public class RegisterDTO {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String repeatPassword;
}
