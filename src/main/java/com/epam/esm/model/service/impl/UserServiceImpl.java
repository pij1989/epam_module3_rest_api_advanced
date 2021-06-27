package com.epam.esm.model.service.impl;

import com.epam.esm.model.dao.OrderDao;
import com.epam.esm.model.dao.UserDao;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final OrderDao orderDao;

    @Autowired
    public UserServiceImpl(UserDao userDao, OrderDao orderDao) {
        this.userDao = userDao;
        this.orderDao = orderDao;
    }

    @Override
    @Transactional
    public Optional<User> createUser(User user) {
        if (user != null) {
            return Optional.of(userDao.create(user));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<User> findUser(Long id) {
        return userDao.findById(id);
    }

    @Override
    @Transactional
    public Optional<User> addOrderToUser(Long userId, Long orderId) {
        Optional<User> optionalUser = userDao.findById(userId);
        if (optionalUser.isPresent()) {
            Optional<Order> optionalOrder = orderDao.findById(orderId);
            if (optionalOrder.isPresent()) {
                User user = optionalUser.get();
                Order order = optionalOrder.get();
                user.addOrder(order);
                return userDao.update(user);
            }
        }
        return Optional.empty();
    }
}
