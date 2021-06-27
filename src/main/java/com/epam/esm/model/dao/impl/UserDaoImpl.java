package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.UserDao;
import com.epam.esm.model.entity.Role;
import com.epam.esm.model.entity.Status;
import com.epam.esm.model.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User create(User entity) {
        Role role = entityManager.createQuery("SELECT r FROM roles r WHERE r.roleType = :name", Role.class)
                .setParameter(ColumnName.NAME, entity.getRole().getRoleType())
                .getSingleResult();
        Status status = entityManager.createQuery("SELECT s FROM statuses s WHERE s.statusType = :name", Status.class)
                .setParameter(ColumnName.NAME, entity.getStatus().getStatusType())
                .getSingleResult();
        entity.setRole(role);
        entity.setStatus(status);
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Optional<User> findById(Long id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public Optional<User> update(User entity) {
        throw new UnsupportedOperationException("Unsupported operation 'update' for UserDao");
    }

    @Override
    public boolean deleteById(Long id) {
        throw new UnsupportedOperationException("Unsupported operation 'delete' for UserDao");
    }
}
