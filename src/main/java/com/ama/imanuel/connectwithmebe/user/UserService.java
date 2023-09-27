package com.ama.imanuel.connectwithmebe.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public void createUser(User admin, CreateUserRequest request) {
     var user = User
             .builder()
             .email(request.getEmail())
             .firstName(request.getFirstName())
             .lastName(request.getLastName())
             .password(passwordEncoder.encode(request.getPassword()))
             .isLocked(false)
             .role(Role.MEMBER)
             .build();
     user.setCreatedBy(admin.getUsername());
     userRepository.save(user);
    }
}
