package com.ama.imanuel.connectwithmebe.config;

import com.ama.imanuel.connectwithmebe.shared.CodeError;
import com.ama.imanuel.connectwithmebe.shared.ErrorAppException;
import com.ama.imanuel.connectwithmebe.shared.ErrorResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class ExceptionAdvice {

    @Value("${application.drees.stacktrace}")
    boolean stackTrace;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse<List<StackTraceElement>> processAllError(Exception ex) {
        List<StackTraceElement> ele = null;

        if (stackTrace) {
            ele = Arrays.asList(ex.getStackTrace());
        }
        return new ErrorResponse<>(ele, ex.getMessage());
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponse<List<StackTraceElement>>> authException(AuthenticationException ex) {
        List<StackTraceElement> ele = null;

        if (stackTrace) {
            ele = Arrays.asList(ex.getStackTrace());
        }
        ErrorResponse<List<StackTraceElement>> response = new ErrorResponse<>(
                ele,
                ex.getMessage(),
                CodeError.FORBIDDEN.getCodeError());

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse<List<StackTraceElement>>> methodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<StackTraceElement> ele = null;

        if (stackTrace) {
            ele = Arrays.asList(ex.getStackTrace());
        }

        final List<String> errors = new ArrayList<>();

        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }


        ErrorResponse<List<StackTraceElement>> response = new ErrorResponse<>(
                ele,
                Strings.join(errors, '\n'),
                CodeError.BAD_REQUEST.getCodeError());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = {ErrorAppException.class})
    @ResponseBody
    protected ResponseEntity<ErrorResponse<List<StackTraceElement>>> processAppException(ErrorAppException exception) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        String codeError = exception.getCodeError();
        if (codeError.startsWith("400")) {
            httpStatus = HttpStatus.BAD_REQUEST;
        } else if (codeError.startsWith("401")) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        } else if (codeError.startsWith("403")) {
            httpStatus = HttpStatus.FORBIDDEN;
        } else if (codeError.startsWith("404")) {
            httpStatus = HttpStatus.NOT_FOUND;
        }
        List<StackTraceElement> ele = null;

        if (stackTrace) {
            ele = Arrays.asList(exception.getStackTrace());
        }

        ErrorResponse<List<StackTraceElement>> response = new ErrorResponse<>(
                ele,
                exception.getMessage(),
                exception.getCodeError());

        return new ResponseEntity<>(response, httpStatus);

    }
}
