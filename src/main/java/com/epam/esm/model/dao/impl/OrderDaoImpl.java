package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.OrderDao;
import com.epam.esm.model.entity.Order;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDaoImpl implements OrderDao {
    private static final String FIND_ALL_ORDERS_JPQL = "SELECT o FROM orders o";

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Order create(Order entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Optional<Order> findById(Long id) {
        Order order = entityManager.find(Order.class, id);
        return Optional.ofNullable(order);
    }

    @Override
    public List<Order> findAll() {
        return entityManager.createQuery(FIND_ALL_ORDERS_JPQL, Order.class)
                .getResultList();
    }

    @Override
    public Optional<Order> update(Order entity) {
        Order order = entityManager.merge(entity);
        return Optional.ofNullable(order);
    }

    @Override
    public boolean deleteById(Long id) {
        throw new UnsupportedOperationException("Unsupported operation 'delete' for OrderDao");
    }

    @Override
    public List<Order> findOrdersWithLimitAndOffset(int offset, int limit) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> root = cq.from(Order.class);
        cq.orderBy(cb.asc(root.get(ColumnName.ID)));
        return entityManager.createQuery(cq.select(root))
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public long countOrder() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(cq.from(Order.class)));
        return entityManager.createQuery(cq).getSingleResult();
    }
}
