package com.epam.esm.model.dao;

import com.epam.esm.model.entity.Order;

import java.util.List;

public interface OrderDao extends BaseDao<Long, Order> {
    List<Order> findOrdersWithLimitAndOffset(int offset, int limit);

    long countOrder();
}
