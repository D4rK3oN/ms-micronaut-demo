package com.example.simple.config.handler;

import com.example.simple.config.annotation.LoggerAdvice;
import com.example.simple.util.FunctionalException;
import com.example.simple.web.response.GlobalExceptionResponse;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import javax.inject.Singleton;

@Produces
@Requires(classes = FunctionalException.class) // This bean loads if FunctionalException are available.
@Singleton
public class FunctionalExceptionHandler implements ExceptionHandler<FunctionalException, HttpResponse<?>> {

    @LoggerAdvice
    @Override
    public HttpResponse<?> handle(HttpRequest request, FunctionalException exception) {
        return HttpResponse
                .status(exception.getExceptionEnum().getHttpStatus())
                .body(GlobalExceptionResponse.builder()
                        .code(exception.getExceptionEnum().getHttpStatus().getCode())
                        .message(exception.getExceptionEnum().getMessage())
                        .detail(exception.getDetail())
                        .build());
    }
}
