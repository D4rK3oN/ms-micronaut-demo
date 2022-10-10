package com.example.simple.web.response;

import com.example.simple.domain.Simple;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@Introspected // This information is use the render the POJO as json using Jackson without using reflection.
public class SimpleResponse {

    @JsonProperty("list")
    private List<Simple> simpleList;
}
