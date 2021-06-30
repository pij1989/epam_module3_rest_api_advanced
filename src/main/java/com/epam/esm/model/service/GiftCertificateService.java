package com.epam.esm.model.service;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Page;
import com.epam.esm.model.entity.Tag;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface GiftCertificateService {
    Optional<GiftCertificate> createGiftCertificate(GiftCertificate giftCertificate);

    Optional<GiftCertificate> findGiftCertificate(Long id);

    List<GiftCertificate> findAllGiftCertificate();

    Page<GiftCertificate> findGiftCertificates(int page, int size);

    Optional<GiftCertificate> updateGiftCertificate(GiftCertificate giftCertificate, Long id);

    Optional<GiftCertificate> updatePartGiftCertificate(GiftCertificate giftCertificate, Long id);

    Optional<Tag> createTagInGiftCertificate(Long certificateId, Tag tag);

    boolean addTagToGiftCertificate(Long certificateId, Long tagId);

    boolean deleteGiftCertificate(Long id);

    Page<GiftCertificate> findGiftCertificateByTagName(String name, int page, int size);

    Page<GiftCertificate> searchGiftCertificate(String filter, int page, int size);

    Page<GiftCertificate> sortGiftCertificate(String sort, String order, int page, int size);

    Optional<GiftCertificate> updatePriceGiftCertificate(BigDecimal price, long id);

    Page<GiftCertificate> searchGiftCertificateByTags(String[] tagNames, int page, int size);
}
