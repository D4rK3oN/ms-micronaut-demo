package com.example.simple.config.handler;

import com.example.simple.config.annotation.LoggerAdvice;
import com.example.simple.util.ExceptionEnum;
import com.example.simple.web.response.GlobalExceptionResponse;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.validation.exceptions.ConstraintExceptionHandler;

import javax.inject.Singleton;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ElementKind;
import javax.validation.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Produces
@Replaces(ConstraintExceptionHandler.class)
@Requires(classes = ConstraintViolationException.class)
@Singleton
public class ConstraintViolationExceptionHandler implements ExceptionHandler<ConstraintViolationException, HttpResponse<?>> {

    @LoggerAdvice
    @Override
    public HttpResponse<?> handle(HttpRequest request, ConstraintViolationException exception) {
        final Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();

        return HttpResponse
                .status(HttpStatus.BAD_REQUEST)
                .body(GlobalExceptionResponse.builder()
                        .code(ExceptionEnum.INVALID_INPUT_PARAMETERS.getHttpStatus().getCode())
                        .message(ExceptionEnum.INVALID_INPUT_PARAMETERS.getMessage())
                        .detail(constraintViolations != null && !constraintViolations.isEmpty() ? getDetail(constraintViolations) : null)
                        .build());
    }

    private String getDetail(Set<ConstraintViolation<?>> constraintViolations) {
        final List<String> errors = constraintViolations
                .stream()
                .map(this::buildMessage)
                .collect(Collectors.toList());

        return errors.toString().substring(1, errors.toString().length() - 1);
    }

    protected String buildMessage(ConstraintViolation violation) {
        Path propertyPath = violation.getPropertyPath();
        StringBuilder message = new StringBuilder();
        Iterator<Path.Node> i = propertyPath.iterator();

        while (i.hasNext()) {
            Path.Node node = i.next();

            if (node.getKind() == ElementKind.METHOD || node.getKind() == ElementKind.CONSTRUCTOR) {
                continue;
            }

            message.append("[" + node.getName() + "]");

            if (i.hasNext()) {
                message.append('.');
            }
        }

        message.append(" -> ").append(violation.getMessage());

        return message.toString();
    }
}
