package com.ama.imanuel.connectwithmebe.shared;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class ErrorDTO<T> implements Serializable {
    private T body;
    private String message;
    @JsonProperty("code_error")
    private String codeError;

    public ErrorDTO(T body, String message) {
        this.body = body;
        this.message = message;
        this.codeError = CodeError.INTERNAL_ERROR.getCodeError();
    }

    public ErrorDTO(T body, String message, String codeError) {
        this.body = body;
        this.message = message;
        this.codeError = codeError;
    }

}
