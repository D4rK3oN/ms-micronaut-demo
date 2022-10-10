package com.example.simple.repository;

import com.example.simple.domain.Simple;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SimpleRepositoryTest implements TestPropertyProvider {

    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer()
            .withDatabaseName("example-test")
            .withUsername("postgres")
            .withPassword("testing");

    static {
        postgreSQLContainer.start();
    }

    @Inject
    SimpleRepository simpleRepository;

    @Inject
    SimpleCrudRepository simpleCrudRepository;

    @Nonnull
    @Override
    public Map<String, String> getProperties() {
        Map<String, String> properties = new HashMap<>();

        properties.put("datasources.default.url", postgreSQLContainer.getJdbcUrl());
        properties.put("datasources.default.username", postgreSQLContainer.getUsername());
        properties.put("datasources.default.password", postgreSQLContainer.getPassword());
        properties.put("datasources.default.driverClassName", postgreSQLContainer.getDriverClassName());

        return properties;
    }

    @Test
    @Transactional
    void findAllWhenNoDataFound() {
        final val response = simpleRepository.findAll(Optional.empty(), Optional.empty(), Optional.empty());

        assertAll(
                () -> assertTrue(response != null && response.isEmpty()),
                () -> assertEquals(0, response.size())
        );
    }

    @Test
    @Transactional
    void findAllWithoutFiltersShouldReturnAllRecords() {
        initTable();

        final val response = simpleRepository.findAll(Optional.empty(), Optional.empty(), Optional.empty());

        assertAll(
                () -> assertTrue(response != null && !response.isEmpty()),
                () -> assertEquals(3, response.size())
        );
    }

    @Test
    @Transactional
    void findAllByNameShouldReturnOneRecord() {
        initTable();

        final val response = simpleRepository.findAll(Optional.of("a th"), Optional.empty(), Optional.empty());

        assertAll(
                () -> assertTrue(response != null && !response.isEmpty()),
                () -> assertEquals(1, response.size()),
                () -> assertEquals(Simple.builder().id(1).name("Neena Thurman").nickname("Domino").build(), response.get(0))
        );
    }

    @Test
    @Transactional
    void findAllByInitAgeShouldReturnTwoRecords() {
        initTable();

        final val response = simpleRepository.findAll(Optional.empty(), Optional.of(5), Optional.empty());

        assertAll(
                () -> assertTrue(response != null && !response.isEmpty()),
                () -> assertEquals(2, response.size())
        );
    }

    @Test
    @Transactional
    void findAllByFinalAgeShouldReturnOneRecord() {
        initTable();

        final val response = simpleRepository.findAll(Optional.empty(), Optional.empty(), Optional.of(30));

        assertAll(
                () -> assertTrue(response != null && !response.isEmpty()),
                () -> assertEquals(1, response.size()),
                () -> assertEquals(Simple.builder().id(0).name("Wade Winston Wilson").nickname("Deadpool").age(28).build(), response.get(0))
        );
    }

    @Test
    @Transactional
    void findAllByInitAgeAndFinalAgeShouldReturnOneRecord() {
        initTable();

        final val response = simpleRepository.findAll(Optional.empty(), Optional.of(30), Optional.of(40));

        assertAll(
                () -> assertTrue(response != null && !response.isEmpty()),
                () -> assertEquals(1, response.size()),
                () -> assertEquals(Simple.builder().id(2).name("Piotr Nikolaievitch Rasputin").nickname("Colossus").age(34).build(), response.get(0))
        );
    }

    @Test
    @Transactional
    void findAllWithAllFiltersShouldReturnOneRecord() {
        initTable();

        final val response = simpleRepository.findAll(Optional.of("niko"), Optional.of(30), Optional.of(40));

        assertAll(
                () -> assertTrue(response != null && !response.isEmpty()),
                () -> assertEquals(1, response.size()),
                () -> assertEquals(Simple.builder().id(2).name("Piotr Nikolaievitch Rasputin").nickname("Colossus").age(34).build(), response.get(0))
        );
    }

    private void initTable() {
        simpleCrudRepository.save(Simple.builder().id(0).name("Wade Winston Wilson").nickname("Deadpool").age(28).build());
        simpleCrudRepository.save(Simple.builder().id(1).name("Neena Thurman").nickname("Domino").build());
        simpleCrudRepository.save(Simple.builder().id(2).name("Piotr Nikolaievitch Rasputin").nickname("Colossus").age(34).build());
    }
}