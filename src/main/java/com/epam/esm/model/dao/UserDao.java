package com.epam.esm.model.dao;

import com.epam.esm.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao extends BaseDao<Long, User> {
    List<User> findUsersWithLimitAndOffset(int offset, int limit);

    Optional<User> findUserWithMaxSumCostOrders();

    long countUser();
}
