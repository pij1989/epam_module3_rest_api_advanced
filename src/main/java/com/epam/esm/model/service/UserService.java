package com.epam.esm.model.service;

import com.epam.esm.model.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> createUser(User user);

    Optional<User> findUser(Long id);
}
