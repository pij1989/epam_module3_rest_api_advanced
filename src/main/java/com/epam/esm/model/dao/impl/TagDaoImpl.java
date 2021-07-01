package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.entity.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDaoImpl implements TagDao {
    private static final String FIND_ALL_TAGS_JPQL = "SELECT t from tags t";
    private static final String FIND_MAX_COUNT_TAG_BY_USER_ID_JPQL = "SELECT t FROM users u JOIN u.orders o JOIN o.orderItems oi JOIN oi.giftCertificate gc JOIN gc.tags t WHERE u.id = :userId GROUP BY t.id ORDER BY count(t.id) DESC";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Tag create(Tag entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Optional<Tag> findById(Long id) {
        Tag tag = entityManager.find(Tag.class, id);
        return Optional.ofNullable(tag);
    }

    @Override
    public List<Tag> findAll() {
        return entityManager.createQuery(FIND_ALL_TAGS_JPQL, Tag.class)
                .getResultList();
    }

    @Override
    public Optional<Tag> update(Tag entity) {
        throw new UnsupportedOperationException("Unsupported operation 'update' for TagDao");
    }

    @Override
    public boolean deleteById(Long id) {
        Tag tag = entityManager.find(Tag.class, id);
        if (tag != null) {
            entityManager.remove(tag);
            return true;
        }
        return false;
    }

    @Override
    public List<Tag> findTagsWithLimitAndOffset(int offset, int limit) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> cq = cb.createQuery(Tag.class);
        Root<Tag> root = cq.from(Tag.class);
        cq.orderBy(cb.asc(root.get(ColumnName.ID)));
        return entityManager.createQuery(cq.select(root))
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public Optional<Tag> findMaxCountTagByUserId(Long userId) {
        Tag tag = entityManager.createQuery(FIND_MAX_COUNT_TAG_BY_USER_ID_JPQL, Tag.class)
                .setParameter("userId", userId)
                .setMaxResults(1)
                .getSingleResult();
        return Optional.ofNullable(tag);
    }

    @Override
    public long countTag() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(cq.from(Tag.class)));
        return entityManager.createQuery(cq).getSingleResult();
    }
}
