package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Sort;
import com.epam.esm.model.entity.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {
    private static final String PERCENT = "%";
    private static final String FIND_GIFT_CERTIFICATES_BY_TAG_NAMES_SQL = "SELECT gc.* FROM tags t JOIN gift_certificate_tags gct ON gct.tag_id = t.id JOIN gift_certificates gc ON gc.id = gct.gift_certificate_id WHERE t.name = ?";
    private static final String FIND_GIFT_CERTIFICATES_BY_TAG_NAME_WITH_OFFSET_AND_LIMIT_JPQL = "SELECT gc FROM gift_certificates gc JOIN gc.tags t WHERE t.name = :name order by gc.id";
    private static final String FIND_ALL_GIFT_CERTIFICATES_JPQL = "SELECT gc FROM gift_certificates gc order by gc.id";
    private static final String COUNT_GIFT_CERTIFICATES_BY_TAG_NAME_JPQL = "SELECT count(t.name) FROM gift_certificates gc JOIN gc.tags t WHERE t.name = :name";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean addTagToCertificate(Long certificateId, Long tagId) {
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, certificateId);
        if (giftCertificate != null) {
            Tag tag = entityManager.find(Tag.class, tagId);
            if (tag != null) {
                giftCertificate.addTag(tag);
                entityManager.flush();
                return true;
            }
        }
        return false;
    }

    @Override
    public Optional<GiftCertificate> updatePart(GiftCertificate giftCertificate) {
        GiftCertificate foundGiftCertificate = entityManager.find(GiftCertificate.class, giftCertificate.getId());
        if (foundGiftCertificate == null) {
            return Optional.empty();
        }
        if (giftCertificate.getName() != null) {
            foundGiftCertificate.setName(giftCertificate.getName());
        }
        if (giftCertificate.getDescription() != null) {
            foundGiftCertificate.setDescription(giftCertificate.getDescription());
        }
        if (giftCertificate.getPrice() != null) {
            foundGiftCertificate.setPrice(giftCertificate.getPrice());
        }
        if (giftCertificate.getDuration() != null) {
            foundGiftCertificate.setDuration(giftCertificate.getDuration());
        }
        if (giftCertificate.getCreateDate() != null) {
            foundGiftCertificate.setCreateDate(giftCertificate.getCreateDate());
        }
        if (giftCertificate.getLastUpdateDate() != null) {
            foundGiftCertificate.setLastUpdateDate(giftCertificate.getLastUpdateDate());
        }
        entityManager.flush();
        return Optional.of(foundGiftCertificate);
    }

    @Override
    public List<GiftCertificate> findGiftCertificatesByTagNameWithOffsetAndLimit(String name, int offset, int limit) {
        return entityManager.createQuery(FIND_GIFT_CERTIFICATES_BY_TAG_NAME_WITH_OFFSET_AND_LIMIT_JPQL, GiftCertificate.class)
                .setParameter(ColumnName.NAME, name)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<GiftCertificate> findGiftCertificateLikeNameOrDescription(String filter, int offset, int limit) {
        String parameter = PERCENT + filter.toUpperCase() + PERCENT;
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> cq = cb.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = cq.from(GiftCertificate.class);
        cq.orderBy(cb.asc(root.get(ColumnName.ID)));
        cq.select(root).where(cb.or(cb.like(cb.upper(root.get(ColumnName.NAME)), parameter),
                cb.like(cb.upper(root.get(ColumnName.DESCRIPTION)), parameter)));
        return entityManager.createQuery(cq)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public GiftCertificate create(GiftCertificate entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Optional<GiftCertificate> findById(Long id) {
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, id);
        return Optional.ofNullable(giftCertificate);
    }

    @Override
    public List<GiftCertificate> findAll() {
        return entityManager.createQuery(FIND_ALL_GIFT_CERTIFICATES_JPQL, GiftCertificate.class)
                .getResultList();
    }

    @Override
    public Optional<GiftCertificate> update(GiftCertificate entity) {
        GiftCertificate giftCertificate = entityManager.merge(entity);
        return Optional.ofNullable(giftCertificate);
    }

    @Override
    public boolean deleteById(Long id) {
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, id);
        if (giftCertificate != null) {
            entityManager.remove(giftCertificate);
            return true;
        }
        return false;
    }

    @Override
    public List<GiftCertificate> findGiftCertificatesWithOffsetAndLimit(int offset, int limit) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> cq = cb.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = cq.from(GiftCertificate.class);
        cq.orderBy(cb.asc(root.get(ColumnName.ID)));
        return entityManager.createQuery(cq.select(root))
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<GiftCertificate> findGiftCertificatesWithOffsetAndLimitOrderBy(int offset, int limit, Sort sort) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> cq = cb.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = cq.from(GiftCertificate.class);
        switch (sort.getDirection()) {
            case ASC: {
                cq.orderBy(cb.asc(root.get(sort.getProperty())));
                break;
            }
            case DESC: {
                cq.orderBy(cb.desc(root.get(sort.getProperty())));
                break;
            }
        }
        return entityManager.createQuery(cq.select(root))
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<GiftCertificate> findGiftCertificateByTagNames(String[] tagNames, int offset, int limit) {
        String sqlQuery = createSqlForFindByTags(tagNames);
        System.out.println(sqlQuery);
        Query query = entityManager.createNativeQuery(sqlQuery, GiftCertificate.class);
        setParameters(tagNames, query);
        @SuppressWarnings("unchecked")
        List<GiftCertificate> giftCertificates = (List<GiftCertificate>) query.getResultList();
        return giftCertificates;
    }

    @Override
    public long countGiftCertificate() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(cq.from(GiftCertificate.class)));
        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    public long countGiftCertificateByTagName(String name) {
        return entityManager.createQuery(COUNT_GIFT_CERTIFICATES_BY_TAG_NAME_JPQL, Long.class)
                .setParameter(ColumnName.NAME, name)
                .getSingleResult();
    }

    @Override
    public long countGiftCertificateLikeNameOrDescription(String filter) {
        String parameter = PERCENT + filter.toUpperCase() + PERCENT;
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<GiftCertificate> root = cq.from(GiftCertificate.class);
        cq.select(cb.count(root)).where(cb.or(cb.like(cb.upper(root.get(ColumnName.NAME)), parameter),
                cb.like(cb.upper(root.get(ColumnName.DESCRIPTION)), parameter)));
        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    public Optional<GiftCertificate> updatePrice(long id, BigDecimal price) {
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, id);
        giftCertificate.setPrice(price);
        entityManager.flush();
        return Optional.of(giftCertificate);
    }

    @Override
    public long countGiftCertificateByTagNames(String[] tagNames) {
        String sqlQuery = createSqlForCountByTags(tagNames);
        Query query = entityManager.createNativeQuery(sqlQuery);
        setParameters(tagNames, query);
        BigInteger result = (BigInteger) query.getSingleResult();
        return result.longValue();
    }

    private String createSqlForFindByTags(String[] tagNames) {
        StringBuilder stringBuilder = new StringBuilder(FIND_GIFT_CERTIFICATES_BY_TAG_NAMES_SQL);
        int count = 0;
        while (count < tagNames.length - 1) {
            stringBuilder.append(" INTERSECT ").append(FIND_GIFT_CERTIFICATES_BY_TAG_NAMES_SQL);
            count++;
        }
        stringBuilder.append(" ORDER BY id");
        return stringBuilder.toString();
    }

    private String createSqlForCountByTags(String[] tagNames) {
        String sqlQuery = createSqlForFindByTags(tagNames);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT count(*) FROM (").append(sqlQuery).append(") AS result");
        return stringBuilder.toString();
    }

    private void setParameters(String[] tagNames, Query query) {
        int count = 1;
        for (String tagName : tagNames) {
            query.setParameter(count, tagName);
            count++;
        }
    }
}
