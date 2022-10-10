package com.example.simple.repository;

import com.example.simple.config.annotation.LoggerAdvice;
import com.example.simple.domain.Simple;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@LoggerAdvice
@Repository
public interface SimpleCrudRepository extends CrudRepository<Simple, Integer> {
}
