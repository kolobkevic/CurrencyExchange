package ru.kolobkevic.currencyexchange.common.repositories;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {
    Optional<T> findById(Integer id);

    List<T> findAll();

    T save(T entity);

    void deleteById(Integer id);
}
