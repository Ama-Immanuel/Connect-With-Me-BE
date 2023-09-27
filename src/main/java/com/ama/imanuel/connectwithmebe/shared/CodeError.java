package com.ama.imanuel.connectwithmebe.shared;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CodeError {
    // Internal server error 500XXX
    INTERNAL_ERROR("50000"),

    // Forbidden error 403XXX
    FORBIDDEN("403000"),

    // Bad Request error 404XXX
    BAD_REQUEST("400000"),
    BAD_REQUEST_PARK_IS_FULL("400001"),

    // Not found error 400XXX
    NOT_FOUND("404000"),
    USER_NOT_FOUND("404001"),

    // UnAuthorized error 401XXX
    UnAuthorized("401000");

    @Getter
    private final String codeError;
}
