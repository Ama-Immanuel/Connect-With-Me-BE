package com.ama.imanuel.connectwithmebe.user;

import com.ama.imanuel.connectwithmebe.shared.BlankResponse;
import com.ama.imanuel.connectwithmebe.shared.SuccessResponse;
import com.ama.imanuel.connectwithmebe.utils.PrincipalUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {

    private final UserService service;
    private final PrincipalUtils principalUtils;

    @PostMapping("/create-user")
    public SuccessResponse<BlankResponse> createUser(@Valid @RequestBody CreateUserRequest request, Principal principal) {
        service.createUser(principalUtils.getUser(principal), request);
        return new SuccessResponse<>(new BlankResponse(), "Success to create user");
    }
}
