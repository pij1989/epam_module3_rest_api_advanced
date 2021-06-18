package com.epam.esm.model.dao;

import com.epam.esm.model.entity.Entity;

import java.util.List;
import java.util.Optional;

public interface BaseDao<K, T extends Entity> {
    T create(T entity);

    Optional<T> findById(K id);

    List<T> findAll();

    Optional<T> update(T entity);

    boolean deleteById(K id);
}
