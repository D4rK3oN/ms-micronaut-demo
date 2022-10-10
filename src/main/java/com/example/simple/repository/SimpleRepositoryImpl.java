package com.example.simple.repository;

import com.example.simple.config.annotation.LoggerAdvice;
import com.example.simple.domain.Simple;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@LoggerAdvice
@Singleton
public class SimpleRepositoryImpl implements SimpleRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Simple> findAll(Optional<String> name, Optional<Integer> initialAge, Optional<Integer> finalAge) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Simple> criteriaQuery = criteriaBuilder.createQuery(Simple.class);

        Root<Simple> root = criteriaQuery.from(Simple.class);

        criteriaQuery.select(root);

        final List<Predicate> predicates = getPredicatesToFindAll(criteriaBuilder, root, name, initialAge, finalAge);

        if (!predicates.isEmpty())
            criteriaQuery.where(predicates.toArray(new Predicate[]{}));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    private List<Predicate> getPredicatesToFindAll(CriteriaBuilder criteriaBuilder, Root<Simple> root,
                                                 Optional<String> name, Optional<Integer> initialAge,
                                                 Optional<Integer> finalAge) {
        List<Predicate> predicates = new ArrayList<>();

        name.ifPresent(value -> predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + value.toLowerCase() + "%")));
        initialAge.ifPresent(value -> predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("age"), value)));
        finalAge.ifPresent(value -> predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("age"), value)));

        return predicates;
    }
}
