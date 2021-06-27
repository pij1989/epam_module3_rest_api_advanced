package com.epam.esm.model.service;

import com.epam.esm.model.entity.Order;

import java.util.Optional;

public interface OrderService {
    Optional<Order> createOrder(Order order);

    Optional<Order> findOrder(Long id);
}
