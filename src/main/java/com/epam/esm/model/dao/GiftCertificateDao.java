package com.epam.esm.model.dao;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Sort;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateDao extends BaseDao<Long, GiftCertificate> {
    boolean addTagToCertificate(Long certificateId, Long tagId);

    Optional<GiftCertificate> updatePart(GiftCertificate giftCertificate);

    List<GiftCertificate> findGiftCertificatesByTagName(String name);

    List<GiftCertificate> findGiftCertificateLikeNameOrDescription(String filter);

    List<GiftCertificate> findGiftCertificatesWithOffsetAndLimit(int offset, int limit);

    List<GiftCertificate> findGiftCertificatesWithOffsetAndLimitOrderBy(int offset, int limit, Sort sort);

    long countGiftCertificate();
}
