package com.example.simple.web.response;

import io.micronaut.core.annotation.Introspected;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Introspected
public class GlobalExceptionResponse {

    private int code;
    private String message;
    private String detail;
}
