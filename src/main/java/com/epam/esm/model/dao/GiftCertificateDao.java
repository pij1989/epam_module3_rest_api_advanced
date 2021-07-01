package com.epam.esm.model.dao;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Sort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface GiftCertificateDao extends BaseDao<Long, GiftCertificate> {
    boolean addTagToCertificate(Long certificateId, Long tagId);

    Optional<GiftCertificate> updatePart(GiftCertificate giftCertificate);

    List<GiftCertificate> findGiftCertificatesByTagNameWithOffsetAndLimit(String name, int offset, int limit);

    List<GiftCertificate> findGiftCertificateLikeNameOrDescription(String filter, int offset, int limit);

    List<GiftCertificate> findGiftCertificatesWithOffsetAndLimit(int offset, int limit);

    List<GiftCertificate> findGiftCertificatesWithOffsetAndLimitOrderBy(int offset, int limit, Sort sort);

    List<GiftCertificate> findGiftCertificateByTagNames(String[] tagNames, int offset, int limit);

    long countGiftCertificate();

    long countGiftCertificateByTagName(String name);

    long countGiftCertificateLikeNameOrDescription(String filter);

    Optional<GiftCertificate> updatePrice(long id, BigDecimal price);

    long countGiftCertificateByTagNames(String[] tagNames);
}
