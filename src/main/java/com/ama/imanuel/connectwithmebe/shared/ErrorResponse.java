package com.ama.imanuel.connectwithmebe.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse<T> {
    private ErrorDTO<T> error;

    public ErrorResponse(T object, String message) {
        error = new ErrorDTO<T>(object, message);
    }

    public ErrorResponse(T object, String message, String codeError) {
        error = new ErrorDTO<T>(object, message, codeError);
    }
}
