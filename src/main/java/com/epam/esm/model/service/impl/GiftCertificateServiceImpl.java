package com.epam.esm.model.service.impl;

import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Page;
import com.epam.esm.model.entity.Sort;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.service.GiftCertificateService;
import com.epam.esm.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {
    public static final Logger logger = LoggerFactory.getLogger(GiftCertificateServiceImpl.class);
    private static final String COMMA_DELIMITER = ",";
    private static final String ASC = "asc";
    private static final String DESC = "desc";
    private static final String NAME = "name";
    private static final String CREATE_DATE = "createDate";
    private final GiftCertificateDao giftCertificateDao;
    private final TagDao tagDao;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao, TagDao tagDao) {
        this.giftCertificateDao = giftCertificateDao;
        this.tagDao = tagDao;
    }

    @Override
    @Transactional
    public Optional<GiftCertificate> createGiftCertificate(GiftCertificate giftCertificate) {
        if (giftCertificate != null) {
            return Optional.of(giftCertificateDao.create(giftCertificate));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<GiftCertificate> findGiftCertificate(Long id) {
        return giftCertificateDao.findById(id);
    }

    @Override
    @Transactional
    public List<GiftCertificate> findAllGiftCertificate() {
        return giftCertificateDao.findAll();
    }

    @Override
    @Transactional
    public Page<GiftCertificate> findGiftCertificates(int page, int size) {
        List<GiftCertificate> giftCertificates = new ArrayList<>();
        int offset = (page - 1) * size;
        long totalElements = giftCertificateDao.countGiftCertificate();
        int totalPages = 0;
        if (totalElements > 0) {
            giftCertificates = giftCertificateDao.findGiftCertificatesWithOffsetAndLimit(offset, size);
            totalPages = PaginationUtil.defineTotalPages(totalElements, size);
        }
        return new Page<>(giftCertificates, totalPages, totalElements, page, size);
    }

    @Override
    @Transactional
    public Optional<GiftCertificate> updateGiftCertificate(GiftCertificate giftCertificate, Long id) {
        if (giftCertificate == null) {
            return Optional.empty();
        }
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateDao.findById(id);
        if (optionalGiftCertificate.isPresent()) {
            giftCertificate.setId(id);
            return giftCertificateDao.update(giftCertificate);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<GiftCertificate> updatePartGiftCertificate(GiftCertificate giftCertificate, Long id) {
        if (giftCertificate == null) {
            return Optional.empty();
        }
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateDao.findById(id);
        if (optionalGiftCertificate.isPresent()) {
            giftCertificate.setId(id);
            return giftCertificateDao.updatePart(giftCertificate);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Tag> createTagInGiftCertificate(Long certificateId, Tag tag) {
        if (tag != null && giftCertificateDao.findById(certificateId).isPresent()) {
            Tag createdTag = tagDao.create(tag);
            Long tagId = createdTag.getId();
            return giftCertificateDao.addTagToCertificate(certificateId, tagId) ? Optional.of(createdTag) : Optional.empty();
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public boolean addTagToGiftCertificate(Long certificateId, Long tagId) {
        Optional<GiftCertificate> optionalGiftCertificate = giftCertificateDao.findById(certificateId);
        if (optionalGiftCertificate.isPresent()) {
            if (tagDao.findById(tagId).isPresent()) {
                return giftCertificateDao.addTagToCertificate(certificateId, tagId);
            }
        }
        return false;
    }

    @Override
    @Transactional
    public boolean deleteGiftCertificate(Long id) {
        return giftCertificateDao.deleteById(id);
    }

    @Override
    @Transactional
    public Page<GiftCertificate> findGiftCertificateByTagName(String name, int page, int size) {
        List<GiftCertificate> certificates = new ArrayList<>();
        int offset = (page - 1) * size;
        long totalElements = giftCertificateDao.countGiftCertificateByTagName(name);
        int totalPages = 0;
        if (totalElements > 0) {
            certificates = giftCertificateDao.findGiftCertificatesByTagNameWithOffsetAndLimit(name, offset, size);
            totalPages = PaginationUtil.defineTotalPages(totalElements, size);
        }
        return new Page<>(certificates, totalPages, totalElements, page, size);
    }

    @Override
    @Transactional
    public List<GiftCertificate> searchGiftCertificate(String filter) {
        return giftCertificateDao.findGiftCertificateLikeNameOrDescription(filter);
    }

    @Override
    @Transactional
    public Page<GiftCertificate> sortGiftCertificate(String sort, int page, int size) {
        List<GiftCertificate> giftCertificates = new ArrayList<>();
        String[] requestParameters = sort.split(COMMA_DELIMITER);
        String type = requestParameters[0].trim();
        String order = requestParameters[1].trim().toUpperCase();
        Sort.Direction direction;
        try {
            direction = Sort.Direction.valueOf(order);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid order of sorting: " + order, e);
            direction = Sort.Direction.ASC;
        }
        Sort sorting = new Sort(type, direction);
        int offset = (page - 1) * size;
        long totalElements = giftCertificateDao.countGiftCertificate();
        int totalPages = 0;
        if (totalElements > 0) {
            giftCertificates = giftCertificateDao.findGiftCertificatesWithOffsetAndLimitOrderBy(offset, size, sorting);
            totalPages = PaginationUtil.defineTotalPages(totalElements, size);
        }
        return new Page<>(giftCertificates, totalPages, totalElements, page, size);
        /*switch (type) {
            case NAME: {
                return sort(giftCertificates, Comparator.comparing(GiftCertificate::getName), order);
            }
            case CREATE_DATE: {
                return sort(giftCertificates, Comparator.comparing(GiftCertificate::getCreateDate), order);
            }
            default: {
                return giftCertificates;
            }
        }*/
    }

    private List<GiftCertificate> sort(List<GiftCertificate> giftCertificates, Comparator<GiftCertificate> comparator, String order) {
        switch (order) {
            case ASC: {
                return giftCertificates.stream()
                        .sorted(comparator)
                        .collect(Collectors.toList());
            }
            case DESC: {
                return giftCertificates.stream()
                        .sorted(comparator.reversed())
                        .collect(Collectors.toList());
            }
            default: {
                return giftCertificates;
            }
        }
    }
}
