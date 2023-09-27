package com.ama.imanuel.connectwithmebe.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    @NotEmpty(message = "first name is required")
    @JsonProperty("first_name")
    String firstName;

    @NotEmpty(message = "last name is required")
    @JsonProperty("last_name")
    String lastName;

    @NotEmpty(message = "email is required")
    @Email(message = "invalid email format", flags = {Pattern.Flag.CASE_INSENSITIVE})
    @JsonProperty("email")
    String email;

    @NotEmpty(message = "password is required")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
            message = "Password must contains lower and uppercase and must contains minimal 1 unique character")
    @JsonProperty("password")
    String password;
}
