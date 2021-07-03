package com.epam.esm.model.service.impl;

import com.epam.esm.model.dao.OrderDao;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dao.UserDao;
import com.epam.esm.model.entity.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceImplTest {
    private User user;
    private Order firstOrder;
    private Order secondOrder;
    private User updatedUser;

    @Mock
    private UserDao userDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private TagDao tagDao;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("First name");
        user.setLastName("Last name");
        user.setUsername("Username");
        user.setEmail("user@gmail.com");
        user.setPassword("m5YQjQifDUhq9wj");
        Role role = new Role();
        role.setRoleType(Role.RoleType.USER);
        user.setRole(role);
        Status status = new Status();
        status.setStatusType(Status.StatusType.ACTIVE);
        user.setStatus(status);
        firstOrder = new Order();
        firstOrder.setId(1L);
        firstOrder.setCost(new BigDecimal("655.57"));
        firstOrder.setCreateDate(LocalDateTime.now());
        user.addOrder(firstOrder);
        secondOrder = new Order();
        secondOrder.setId(2L);
        secondOrder.setCost(new BigDecimal("700.00"));
        secondOrder.setCreateDate(LocalDateTime.now());
        updatedUser = new User();
        updatedUser.setId(user.getId());
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setUsername(user.getUsername());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setRole(user.getRole());
        updatedUser.setStatus(user.getStatus());
        updatedUser.setOrders(user.getOrders());
        updatedUser.addOrder(secondOrder);
    }

    @AfterEach
    void tearDown() {
        user = null;
        firstOrder = null;
        secondOrder = null;
        updatedUser = null;
    }

    @Test
    void createUser() {
        when(userDao.create(user)).thenReturn(user);
        Optional<User> actual = userService.createUser(user);
        Optional<User> expect = Optional.of(user);
        assertEquals(expect, actual);
    }

    @Test
    void findUser() {
        when(userDao.findById(1L)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findUser(1L);
        Optional<User> expect = Optional.of(user);
        assertEquals(expect, actual);
    }

    @Test
    void addOrderToUser() {
        when(userDao.findById(1L)).thenReturn(Optional.of(user));
        when(orderDao.findById(2L)).thenReturn(Optional.of(secondOrder));
        when(userDao.update(user)).thenReturn(Optional.of(updatedUser));
        Optional<User> actual = userService.addOrderToUser(1L, 2L);
        Optional<User> expect = Optional.of(updatedUser);
        assertEquals(expect, actual);
    }

    @Test
    void createOrderForUser() {
        when(userDao.findById(1L)).thenReturn(Optional.of(user));
        when(orderDao.create(secondOrder)).thenReturn(secondOrder);
        when(userDao.update(user)).thenReturn(Optional.of(updatedUser));
        Optional<Order> actual = userService.createOrderForUser(1L, secondOrder);
        Optional<Order> expect = Optional.of(secondOrder);
        assertEquals(expect, actual);
    }

    @Test
    void findUsers() {
        when(userDao.countUser()).thenReturn(1L);
        when(userDao.findUsersWithLimitAndOffset(0, 5)).thenReturn(List.of(user));
        Page<User> expect = new Page<>(List.of(user), 1, 1, 1, 5);
        Page<User> actual = userService.findUsers(1, 5);
        assertEquals(expect, actual);
    }

    @Test
    void findOrdersForUser() {
        when(userDao.findById(1L)).thenReturn(Optional.of(user));
        when(orderDao.countOrder()).thenReturn(1L);
        List<Order> orders = new ArrayList<>(user.getOrders());
        when(orderDao.findOrdersWithLimitAndOffset(0, 5)).thenReturn(orders);
        Page<Order> expect = new Page<>(orders, 1, 1, 1, 5);
        Page<Order> actual = userService.findOrdersForUser(1L, 1, 5);
        assertEquals(expect, actual);
    }

    @Test
    void findOrderForUser() {
        when(userDao.findById(1L)).thenReturn(Optional.of(user));
        when(orderDao.findById(1L)).thenReturn(Optional.of(firstOrder));
        Optional<Order> actual = userService.findOrderForUser(1L, 1L);
        Optional<Order> expect = Optional.of(firstOrder);
        assertEquals(expect, actual);
    }

    @Test
    void findWidelyUsedTagForUserWithHighestCostOfAllOrders() {
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("Tag name");
        when(userDao.findUserWithMaxSumCostOrders()).thenReturn(Optional.of(user));
        when(tagDao.findMaxCountTagByUserId(1L)).thenReturn(Optional.of(tag));
        Optional<Tag> actual = userService.findWidelyUsedTagForUserWithHighestCostOfAllOrders();
        Optional<Tag> expect = Optional.of(tag);
        assertEquals(expect, actual);
    }
}