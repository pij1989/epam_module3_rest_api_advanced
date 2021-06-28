package com.epam.esm.model.dao;

import com.epam.esm.model.entity.User;

import java.util.List;

public interface UserDao extends BaseDao<Long, User> {
    List<User> findUsersWithLimitAndOffset(int offset, int limit);

    long countUser();
}
