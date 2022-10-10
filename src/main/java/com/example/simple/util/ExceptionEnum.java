package com.example.simple.util;

import io.micronaut.http.HttpStatus;
import lombok.Getter;

public enum ExceptionEnum {

    CONFLICT(HttpStatus.CONFLICT, "The request could not be completed due to a conflict with the current state of the resource."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Technical error in the system"),
    INVALID_INPUT_PARAMETERS(HttpStatus.BAD_REQUEST, "Invalid input parameters"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Required request is missing"),
    NO_AUTH_ACCESS(HttpStatus.UNAUTHORIZED, "Unauthorized"),
    NO_AUTH_INFO(HttpStatus.NON_AUTHORITATIVE_INFORMATION, "Unauthorized"),
    NO_CONTENT(HttpStatus.NO_CONTENT, "No content"),
    NO_DATA_FOUND(HttpStatus.NOT_FOUND, "No data found");

    @Getter
    private HttpStatus httpStatus;

    @Getter
    private String message;

    ExceptionEnum(final HttpStatus httpStatus, final String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
