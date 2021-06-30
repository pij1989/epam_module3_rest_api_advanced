package com.epam.esm.model.service.impl;

import com.epam.esm.model.dao.OrderDao;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dao.UserDao;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.Page;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.entity.User;
import com.epam.esm.model.service.UserService;
import com.epam.esm.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final OrderDao orderDao;
    private final TagDao tagDao;

    @Autowired
    public UserServiceImpl(UserDao userDao, OrderDao orderDao, TagDao tagDao) {
        this.userDao = userDao;
        this.orderDao = orderDao;
        this.tagDao = tagDao;
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

    @Override
    @Transactional
    public Optional<Order> createOrderForUser(Long userId, Order order) {
        Optional<User> optionalUser = userDao.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Order createdOrder = orderDao.create(order);
            user.addOrder(createdOrder);
            userDao.update(user);
            return Optional.of(createdOrder);
        }
        return Optional.empty();
    }

    @Override
    public Page<User> findUsers(int page, int size) {
        List<User> users = new ArrayList<>();
        int offset = (page - 1) * size;
        long totalElements = userDao.countUser();
        int totalPages = 0;
        if (totalElements > 0) {
            users = userDao.findUsersWithLimitAndOffset(offset, size);
            totalPages = PaginationUtil.defineTotalPages(totalElements, size);
        }
        return new Page<>(users, totalPages, totalElements, page, size);
    }

    @Override
    public Page<Order> findOrdersForUser(Long userId, int page, int size) {
        List<Order> orders = new ArrayList<>();
        Page<Order> createdPage = new Page<>();
        Optional<User> optionalUser = userDao.findById(userId);
        if (optionalUser.isPresent()) {
            int offset = (page - 1) * size;
            long totalElements = orderDao.countOrder();
            int totalPages = 0;
            if (totalElements > 0) {
                orders = orderDao.findOrdersWithLimitAndOffset(offset, size);
                totalPages = PaginationUtil.defineTotalPages(totalElements, size);
            }
            createdPage = new Page<>(orders, totalPages, totalElements, page, size);
        }
        return createdPage;
    }

    @Override
    public Optional<Order> findOrderForUser(Long userId, Long orderId) {
        Optional<User> optionalUser = userDao.findById(userId);
        if (optionalUser.isPresent()) {
            return orderDao.findById(orderId);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Tag> findWidelyUsedTagForUserWithHighestCostOfAllOrders() {
        Optional<User> optionalUser = userDao.findUserWithMaxSumCostOrders();
        return optionalUser.map(User::getId)
                .flatMap(tagDao::findMaxCountTagByUserId);
    }
}
