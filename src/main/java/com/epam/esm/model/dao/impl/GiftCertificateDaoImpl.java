package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {

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
        return Optional.empty();
    }

    @Override
    public List<GiftCertificate> findGiftCertificatesByTagName(String name) {
        return null;
    }

    @Override
    public List<GiftCertificate> findGiftCertificateLikeNameOrDescription(String filter) {
        return null;
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
        return entityManager.createQuery("SELECT gc FROM gift_certificate gc", GiftCertificate.class)
                .getResultList();
    }

    @Override
    public Optional<GiftCertificate> update(GiftCertificate entity) {
        GiftCertificate giftCertificate = entityManager.merge(entity);
        return Optional.ofNullable(giftCertificate);
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }
}
