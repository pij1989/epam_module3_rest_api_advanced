package com.epam.esm.model.service;

import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.Page;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> createUser(User user);

    Optional<User> findUser(Long id);

    Optional<User> addOrderToUser(Long userId, Long orderId);

    Optional<Order> createOrderForUser(Long userId, Order order);

    Page<User> findUsers(int page, int size);

    Page<Order> findOrdersForUser(Long userId, int page, int size);

    Optional<Order> findOrderForUser(Long userId, Long orderId);

    Optional<Tag> findWidelyUsedTagForUserWithHighestCostOfAllOrders();
}
