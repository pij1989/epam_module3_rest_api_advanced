package com.epam.esm.model.service.impl;

import com.epam.esm.model.dao.OrderDao;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    @Transactional
    public Optional<Order> createOrder(Order order) {
        if (order != null) {
            Order createdOrder = orderDao.create(order);
            return Optional.of(createdOrder);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Order> findOrder(Long id) {
        return orderDao.findById(id);
    }
}
