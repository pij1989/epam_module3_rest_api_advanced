package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.UserDao;
import com.epam.esm.model.entity.Role;
import com.epam.esm.model.entity.Status;
import com.epam.esm.model.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
        return entityManager.createQuery("SELECT u FROM users u", User.class)
                .getResultList();
    }

    @Override
    public Optional<User> update(User entity) {
        User user = entityManager.merge(entity);
        return Optional.ofNullable(user);
    }

    @Override
    public boolean deleteById(Long id) {
        throw new UnsupportedOperationException("Unsupported operation 'delete' for UserDao");
    }

    @Override
    public List<User> findUsersWithLimitAndOffset(int offset, int limit) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.orderBy(cb.asc(root.get(ColumnName.ID)));
        return entityManager.createQuery(cq.select(root))
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public long countUser() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(cq.from(User.class)));
        return entityManager.createQuery(cq).getSingleResult();
    }
}
