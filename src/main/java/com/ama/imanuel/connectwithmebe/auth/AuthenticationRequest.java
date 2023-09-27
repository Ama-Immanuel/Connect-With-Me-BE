package com.ama.imanuel.connectwithmebe.auth;


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
public class AuthenticationRequest {

    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid format for email address", flags = {Pattern.Flag.CASE_INSENSITIVE})
    private String email;

    @NotEmpty(message = "Password is required")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "password must contain upper and lower char, number and uniq character")
    String password;
}
