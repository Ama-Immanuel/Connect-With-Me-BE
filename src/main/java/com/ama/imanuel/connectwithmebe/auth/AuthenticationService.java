package com.ama.imanuel.connectwithmebe.auth;

import com.ama.imanuel.connectwithmebe.config.JwtService;
import com.ama.imanuel.connectwithmebe.shared.CodeError;
import com.ama.imanuel.connectwithmebe.shared.ErrorAppException;
import com.ama.imanuel.connectwithmebe.token.Token;
import com.ama.imanuel.connectwithmebe.token.TokenRepository;
import com.ama.imanuel.connectwithmebe.token.TokenType;
import com.ama.imanuel.connectwithmebe.user.Role;
import com.ama.imanuel.connectwithmebe.user.User;
import com.ama.imanuel.connectwithmebe.user.UserRepository;
import com.ama.imanuel.connectwithmebe.utils.DateFormatUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final DateFormatUtils dateFormatUtils;


    @Value("${application.security.jwt.refresh-key}")
    private String secretKeyRefresh;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));
        var user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(
                        () -> new ErrorAppException(
                                CodeError.USER_NOT_FOUND.getCodeError(),
                                "User not found"));
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken.getToken());
        return AuthenticationResponse
                .builder()
                .accessToken(jwtToken.getToken())
                .refreshToken(refreshToken.getToken())
                .expiredToken(jwtToken.getTimeExpired())
                .build();
    }

    public AuthenticationResponse registerUser(RegisterRequest request) throws ParseException {
        var user = User.builder()
                .email(request.getEmail())
                .role(Role.MEMBER)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .profile(request.getProfile())
                .password(passwordEncoder.encode(request.getPassword()))
                .isLocked(false)
                .dateOfBirth(dateFormatUtils.convertToDateSqlFromString(request.getDateOfBirth()))
                .build();

        user.setCreatedBy("SYSTEM");
        user = userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken.getToken());
        return AuthenticationResponse
                .builder()
                .accessToken(jwtToken.getToken())
                .refreshToken(refreshToken.getToken())
                .expiredToken(jwtToken.getTimeExpired())
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token
                .builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new ErrorAppException(CodeError.FORBIDDEN.getCodeError(), "Token is required");

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken, secretKeyRefresh);
        if (userEmail != null) {
            var user = this.userRepository
                    .findByEmail(userEmail)
                    .orElseThrow(() -> new ErrorAppException(
                            CodeError.USER_NOT_FOUND.getCodeError(),
                            "User not found"));
            if (jwtService.isTokenValid(refreshToken, user, secretKeyRefresh)) {
                var accessToken = jwtService.generateToken(user);
                var newRefreshToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken.getToken());
                return AuthenticationResponse
                        .builder()
                        .accessToken(accessToken.getToken())
                        .refreshToken(newRefreshToken.getToken())
                        .expiredToken(newRefreshToken.getTimeExpired())
                        .build();
            }
        }
        throw new ErrorAppException(CodeError.FORBIDDEN.getCodeError(), "invalid refresh token");
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(false);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
