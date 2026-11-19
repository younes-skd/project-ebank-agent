package ma.emsi.app;

import jakarta.validation.constraints.*;
public record CustomerRequest(@NotBlank String name, @NotBlank @Email String email) {}
