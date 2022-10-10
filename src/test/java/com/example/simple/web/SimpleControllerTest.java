package com.example.simple.web;

import com.example.simple.domain.Simple;
import com.example.simple.service.SimpleService;
import com.example.simple.service.SimpleServiceImpl;
import com.example.simple.util.ExceptionEnum;
import com.example.simple.util.FunctionalException;
import com.example.simple.web.response.GlobalExceptionResponse;
import com.example.simple.web.response.SimpleResponse;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import lombok.val;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MicronautTest
class SimpleControllerTest {

    private static final String PATH = "/example";
    private static final List<Simple> SIMPLE_LIST_OK = Arrays.asList(
            Simple.builder().id(1).name("Neena Thurman").email("domino@xforce.com").build(),
            Simple.builder().id(2).nickname("Cable").build(),
            Simple.builder().id(3).nickname("Psylocke").build(),
            Simple.builder().id(4).nickname("Colossus").build(),
            Simple.builder().id(5).name("Wade Winston Wilson").nickname("Deadpool").age(28).build()
    );

    @Inject
    SimpleService simpleService;

    @Inject
    @Client("/")
    HttpClient client;

    @MockBean(SimpleServiceImpl.class)
    SimpleService simpleService() {
        return mock(SimpleService.class);
    }

    @Test
    void findAllSimpleWhenOk() {
        when(simpleService.findAllSimple(Optional.empty(), Optional.empty(), Optional.empty()))
                .thenReturn(SIMPLE_LIST_OK);

        val response = client.toBlocking().exchange(HttpRequest.GET(PATH), SimpleResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.getCode(), response.code()),
                () -> assertEquals(HttpStatus.OK, response.status()),
                () -> assertNotNull(response.body()),
                () -> assertEquals(5, response.getBody().orElse(SimpleResponse.builder().build()).getSimpleList().size()),
                () -> assertEquals(SimpleResponse.builder().simpleList(SIMPLE_LIST_OK).build(), response.body())
        );
    }

    @Test
    void findAllSimpleWhenNoDataFound() {
        when(simpleService.findAllSimple(Optional.empty(), Optional.empty(), Optional.empty()))
                .thenReturn(Collections.emptyList());

        val response = client.toBlocking().exchange(HttpRequest.GET(PATH), SimpleResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.getCode(), response.code()),
                () -> assertEquals(HttpStatus.OK, response.status()),
                () -> assertNotNull(response.body()),
                () -> assertNull(response.body().getSimpleList())
        );
    }

    @Test
    void findAllSimpleWhenFiltersAreOk() {
        when(simpleService.findAllSimple(Optional.of("Wade"), Optional.of(20), Optional.of(30)))
                .thenReturn(Collections.singletonList(SIMPLE_LIST_OK.get(4)));

        final URI uri = UriBuilder.of(PATH)
                .queryParam("name", "Wade")
                .queryParam("initialAge", 20)
                .queryParam("finalAge", 30)
                .build();

        val response = client.toBlocking().exchange(HttpRequest.GET(uri), SimpleResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.getCode(), response.code()),
                () -> assertEquals(HttpStatus.OK, response.status()),
                () -> assertNotNull(response.body()),
                () -> assertEquals(1, response.getBody().orElse(SimpleResponse.builder().build()).getSimpleList().size()),
                () -> assertEquals(SimpleResponse.builder().simpleList(Collections.singletonList(SIMPLE_LIST_OK.get(4))).build(), response.body())
        );
    }

    // TODO: Execution break by HttpClientResponseException
//    @Test
    void findAllSimpleWhenFiltersAreWrong() {
        final URI uri = UriBuilder.of(PATH)
                .queryParam("name", "w")
                .build();

        val response = client.toBlocking().exchange(HttpRequest.GET(uri));

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.getCode(), response.code()),
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.status()),
                () -> assertNotNull(response.body())
        );
    }

    @Test
    void findSimpleByIdWhenOk() {
        when(simpleService.findSimpleById(1))
                .thenReturn(SIMPLE_LIST_OK.get(0));

        val response = client.toBlocking().exchange(HttpRequest.GET(PATH + "/1"), Simple.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.getCode(), response.code()),
                () -> assertEquals(HttpStatus.OK, response.status()),
                () -> assertNotNull(response.body()),
                () -> assertEquals(SIMPLE_LIST_OK.get(0), response.body())
        );
    }

    // TODO: Execution break by HttpClientResponseException
//    @Test
    void findSimpleByIdWhenNoDataFound() {
        when(simpleService.findSimpleById(1))
                .thenThrow(new FunctionalException(
                        "Not valid findById response",
                        ExceptionEnum.NO_DATA_FOUND,
                        "ID [1] not exist"));

        val response = client.toBlocking().exchange(HttpRequest.GET(PATH + "/1"), GlobalExceptionResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.getCode(), response.code()),
                () -> assertEquals(HttpStatus.NOT_FOUND, response.status()),
                () -> assertNotNull(response.body()),
                () -> assertEquals(GlobalExceptionResponse.builder().build(), response.body())
        );
    }
}