package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.OrderItemDao;
import com.epam.esm.model.entity.OrderItem;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderItemDaoImpl implements OrderItemDao {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public OrderItem create(OrderItem entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Optional<OrderItem> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<OrderItem> findAll() {
        return null;
    }

    @Override
    public Optional<OrderItem> update(OrderItem entity) {
        return Optional.empty();
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }
}
