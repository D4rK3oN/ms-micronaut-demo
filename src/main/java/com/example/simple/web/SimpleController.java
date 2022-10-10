package com.example.simple.web;

import com.example.simple.config.annotation.LoggerAdvice;
import com.example.simple.domain.Simple;
import com.example.simple.service.SimpleService;
import com.example.simple.web.response.SimpleResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.Optional;

@Controller("/example")
@LoggerAdvice
@RequiredArgsConstructor
public class SimpleController {

    private final SimpleService simpleService;

    @Get
    public SimpleResponse findAllSimple(
            @QueryValue @Nullable @Size(min = 3, message = "the length of the name must be 3 or greater") final String name,
            @QueryValue @Nullable @PositiveOrZero final Integer initialAge,
            @QueryValue @Nullable @PositiveOrZero final Integer finalAge
    ) {
        return SimpleResponse.builder()
                .simpleList(
                        simpleService.findAllSimple(
                                Optional.ofNullable(name),
                                Optional.ofNullable(initialAge),
                                Optional.ofNullable(finalAge)
                        )
                )
                .build();
    }

    @Get(value = "/{id}")
    public Simple findSimpleById(Integer id) {
        return simpleService.findSimpleById(id);
    }
}
