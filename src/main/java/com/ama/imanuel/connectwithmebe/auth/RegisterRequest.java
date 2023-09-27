package com.ama.imanuel.connectwithmebe.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotEmpty(message = "first name is required")
    @JsonProperty(value = "first_name", defaultValue = "Andree")
    String firstName;

    @NotEmpty(message = "last name is required")
    @JsonProperty(value = "last_name", defaultValue = "Panjaitan")
    String lastName;

    @NotEmpty(message = "email is required")
    @Email(message = "invalid email format", flags = {Pattern.Flag.CASE_INSENSITIVE})
    @JsonProperty(value = "email", defaultValue = "example@gmail.com")
    String email;

    @NotEmpty(message = "date of birth is required")
    @Pattern(regexp = "\\d{4}-[01]\\d-[0-3]\\d", message = "invalid date format")
    @JsonProperty(value = "date_of_birth", defaultValue = "2006-01-23")
    String dateOfBirth;

    @JsonProperty(value = "profile", defaultValue = "example.png")
    String profile;

    @NotEmpty(message = "password is required")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
            message = "Password must contains lower and uppercase and must contains minimal 1 unique character")
    @JsonProperty(value = "password", defaultValue = "Password@1234")
    String password;
}
