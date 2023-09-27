package com.ama.imanuel.connectwithmebe.auth;

import com.ama.imanuel.connectwithmebe.shared.SuccessResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@Controller
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/authentication")
    public SuccessResponse<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        return new SuccessResponse<>(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public SuccessResponse<AuthenticationResponse> refreshToken(HttpServletRequest request) {
        return new SuccessResponse<>(service.refreshToken(request));
    }

    @PostMapping("/register")
    public SuccessResponse<AuthenticationResponse> registerUser(
            @Valid @RequestBody RegisterRequest request
    ) throws ParseException {
        return new SuccessResponse<>(service.registerUser(request));
    }

}
