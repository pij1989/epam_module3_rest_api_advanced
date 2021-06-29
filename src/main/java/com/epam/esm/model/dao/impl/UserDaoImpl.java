package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.UserDao;
import com.epam.esm.model.entity.Role;
import com.epam.esm.model.entity.Status;
import com.epam.esm.model.entity.Tag;
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
    //    SELECT t.id, count(t.id) FROM users u JOIN orders o ON o.user_id = u.id JOIN order_items oi ON oi.order_id = o.id JOIN gift_certificates gc ON gc.id = oi.gift_certificate_id JOIN gift_certificate_tags gct ON gct.gift_certificate_id = gc.id JOIN tags t ON t.id = gct.tag_id WHERE u.id = (SELECT u.id FROM users u JOIN orders o ON o.user_id = u.id GROUP BY u.id ORDER BY SUM(cost) DESC LIMIT 1) GROUP BY t.id ORDER BY count(t.id) DESC LIMIT 1;
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
    public Tag findWidelyUsedTagForUserWithHighestCostOfAllOrders() {
        Long userId = entityManager.createQuery("SELECT u.id FROM users u JOIN u.orders o GROUP BY u.id ORDER BY sum(o.cost) DESC", Long.class)
                .setMaxResults(1)
                .getSingleResult();
        return entityManager.createQuery("SELECT t FROM users u JOIN u.orders o JOIN o.orderItems oi JOIN oi.giftCertificate gc JOIN gc.tags t WHERE u.id = :userId GROUP BY t.id ORDER BY count(t.id) DESC", Tag.class)
                .setParameter("userId", userId)
                .setMaxResults(1)
                .getSingleResult();
    }

    @Override
    public long countUser() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(cq.from(User.class)));
        return entityManager.createQuery(cq).getSingleResult();
    }
}
