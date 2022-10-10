package com.example.simple.service;

import com.example.simple.domain.Simple;
import com.example.simple.repository.SimpleCrudRepository;
import com.example.simple.repository.SimpleRepository;
import com.example.simple.util.FunctionalException;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimpleServiceTest {

    private static final List<Simple> SIMPLE_LIST_OK = Arrays.asList(
            Simple.builder().id(1).name("Domino").build(),
            Simple.builder().id(2).name("Cable").build(),
            Simple.builder().id(3).name("Psylocke").build(),
            Simple.builder().id(4).name("Colossus").build(),
            Simple.builder().id(5).name("Deadpool").age(28).build()
    );

    @Mock
    private SimpleRepository simpleRepository;

    @Mock
    private SimpleCrudRepository simpleCrudRepository;

    @InjectMocks
    private SimpleServiceImpl simpleService;

    @Test
    void findAllSimpleShouldReturnTheSameAsTheRepository() {
        when(simpleRepository.findAll(any(), any(), any())).thenReturn(SIMPLE_LIST_OK);

        final val response = simpleService.findAllSimple(Optional.empty(), Optional.empty(), Optional.empty());

        assertAll(
                () -> assertTrue(response != null && !response.isEmpty()),
                () -> assertEquals(5, response.size()),
                () -> assertEquals(SIMPLE_LIST_OK, response)
        );
    }

    @Test
    void findSimpleByIdWhenExistData() {
        when(simpleCrudRepository.findById(1))
                .thenReturn(Optional.of(Simple.builder().id(1).name("Domino").build()));

        final val response = simpleService.findSimpleById(1);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(Simple.builder().id(1).name("Domino").build(), response)
        );
    }

    @Test
    void findSimpleByIdWhenNoDataFound() {
        when(simpleCrudRepository.findById(0)).thenReturn(Optional.empty());

        assertThrows(FunctionalException.class, () -> simpleService.findSimpleById(0));
    }
}