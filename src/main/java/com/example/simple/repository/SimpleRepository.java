package com.example.simple.repository;

import com.example.simple.domain.Simple;

import java.util.List;
import java.util.Optional;

public interface SimpleRepository {

    List<Simple> findAll(Optional<String> name, Optional<Integer> initialAge, Optional<Integer> finalAge);
}
