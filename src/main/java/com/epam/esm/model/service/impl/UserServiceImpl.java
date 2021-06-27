package com.epam.esm.model.service.impl;

import com.epam.esm.model.dao.UserDao;
import com.epam.esm.model.entity.User;
import com.epam.esm.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
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
}
