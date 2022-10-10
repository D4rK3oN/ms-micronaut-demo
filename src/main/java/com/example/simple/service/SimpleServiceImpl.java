package com.example.simple.service;

import com.example.simple.config.annotation.LoggerAdvice;
import com.example.simple.domain.Simple;
import com.example.simple.repository.SimpleCrudRepository;
import com.example.simple.repository.SimpleRepository;
import com.example.simple.util.ExceptionEnum;
import com.example.simple.util.FunctionalException;
import lombok.RequiredArgsConstructor;
import lombok.val;

import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@LoggerAdvice
@RequiredArgsConstructor
@Singleton
public class SimpleServiceImpl implements SimpleService {

    private final SimpleRepository simpleRepository;
    private final SimpleCrudRepository simpleCrudRepository;

    @Override
    @Transactional
    public List<Simple> findAllSimple(Optional<String> name, Optional<Integer> initialAge, Optional<Integer> finalAge) {
        return simpleRepository.findAll(name, initialAge, finalAge);
    }

    @Override
    @Transactional
    public Simple findSimpleById(Integer simpleId) throws FunctionalException {
        final val simple = simpleCrudRepository.findById(simpleId);

        if (!simple.isPresent())
            throw new FunctionalException(
                    "Not valid findById response",
                    ExceptionEnum.NO_DATA_FOUND,
                    "ID [".concat(String.valueOf(simpleId)).concat("] not exist"));

        return simple.get();
    }
}
