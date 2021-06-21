package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.entity.GiftCertificate;
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
public class GiftCertificateDaoImpl implements GiftCertificateDao {
    private static final String PERCENT = "%";

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
    public List<GiftCertificate> findGiftCertificatesByTagName(String name) {
        return entityManager.createQuery("SELECT gc FROM gift_certificate gc JOIN gc.tags t WHERE t.name = :name order by gc.id", GiftCertificate.class)
                .setParameter(ColumnName.NAME, name)
                .getResultList();
    }

    @Override
    public List<GiftCertificate> findGiftCertificateLikeNameOrDescription(String filter) {
        String parameter = PERCENT + filter.toLowerCase() + PERCENT;
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> cq = cb.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = cq.from(GiftCertificate.class);
        cq.select(root).where(cb.or(cb.like(root.get(ColumnName.NAME), parameter),
                cb.like(root.get(ColumnName.DESCRIPTION), parameter)));
        return entityManager.createQuery(cq).getResultList();
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
        return entityManager.createQuery("SELECT gc FROM gift_certificate gc order by gc.id", GiftCertificate.class)
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
}
