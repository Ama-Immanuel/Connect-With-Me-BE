package com.ama.imanuel.connectwithmebe.utils;


import com.ama.imanuel.connectwithmebe.shared.CodeError;
import com.ama.imanuel.connectwithmebe.shared.ErrorAppException;
import com.ama.imanuel.connectwithmebe.user.User;
import com.ama.imanuel.connectwithmebe.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class PrincipalUtils {
    private final UserRepository userRepository;

    public User getUser(Principal principal) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
        var sec = (User) usernamePasswordAuthenticationToken.getPrincipal();
        return userRepository.findByEmail(sec.getEmail())
                .orElseThrow(() ->
                        new ErrorAppException(CodeError.USER_NOT_FOUND.getCodeError(), "user not found"));
    }
}
