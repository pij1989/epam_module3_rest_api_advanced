package com.epam.esm.model.service;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateService {
    Optional<GiftCertificate> createGiftCertificate(GiftCertificate giftCertificate);

    Optional<GiftCertificate> findGiftCertificate(Long id);

    List<GiftCertificate> findAllGiftCertificate();

    Optional<GiftCertificate> updateGiftCertificate(GiftCertificate giftCertificate, Long id);

    Optional<GiftCertificate> updatePartGiftCertificate(GiftCertificate giftCertificate, Long id);

    Optional<Tag> createTagInGiftCertificate(Long certificateId, Tag tag);

    boolean addTagToGiftCertificate(Long certificateId, Long tagId);

    boolean deleteGiftCertificate(Long id);

    List<GiftCertificate> findGiftCertificateByTagName(String name);

    List<GiftCertificate> searchGiftCertificate(String filter);

    List<GiftCertificate> sortGiftCertificate(String sort);
}
