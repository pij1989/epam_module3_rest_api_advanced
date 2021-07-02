package com.epam.esm.model.service.impl;

import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Page;
import com.epam.esm.model.entity.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class GiftCertificateServiceImplTest {
    private GiftCertificate giftCertificate;

    @Mock
    private GiftCertificateDao giftCertificateDao;

    @Mock
    private TagDao tagDao;

    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    @BeforeEach
    void setUp() {
        giftCertificate = new GiftCertificate();
        giftCertificate.setName("New gift certificate name");
        giftCertificate.setDescription("New gift certificate description");
        giftCertificate.setPrice(new BigDecimal("55.77"));
        giftCertificate.setDuration(30);
        giftCertificate.setCreateDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
    }

    @AfterEach
    void tearDown() {
        giftCertificate = null;
    }

    @Test
    void createGiftCertificate() {
        when(giftCertificateDao.create(giftCertificate)).thenReturn(giftCertificate);
        Optional<GiftCertificate> actual = giftCertificateService.createGiftCertificate(giftCertificate);
        verify(giftCertificateDao, times(1)).create(giftCertificate);
        assertEquals(Optional.of(giftCertificate), actual);
    }

    @Test
    void findGiftCertificate() {
        when(giftCertificateDao.findById(1L)).thenReturn(Optional.of(giftCertificate));
        Optional<GiftCertificate> actual = giftCertificateService.findGiftCertificate(1L);
        assertEquals(Optional.of(giftCertificate), actual);
    }

    @Test
    void findAllGiftCertificate() {
        when(giftCertificateDao.findAll()).thenReturn(List.of(new GiftCertificate(), new GiftCertificate()));
        List<GiftCertificate> actual = giftCertificateService.findAllGiftCertificate();
        verify(giftCertificateDao, times(1)).findAll();
        assertEquals(2, actual.size());
    }

    @Test
    void updateGiftCertificate() {
        giftCertificate.setName("Updated gift certificate name");
        giftCertificate.setDescription("Updated gift certificate description");
        when(giftCertificateDao.update(giftCertificate)).thenReturn(Optional.of(giftCertificate));
        when(giftCertificateDao.findById(1L)).thenReturn(Optional.of(giftCertificate));
        Optional<GiftCertificate> actual = giftCertificateService.updateGiftCertificate(giftCertificate, 1L);
        verify(giftCertificateDao, times(1)).update(giftCertificate);
        assertEquals(Optional.of(giftCertificate), actual);
    }

    @Test
    void createTagInGiftCertificate() {
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("Tag name");
        when(giftCertificateDao.findById(1L)).thenReturn(Optional.of(new GiftCertificate()));
        when(giftCertificateDao.addTagToCertificate(1L, 1L)).thenReturn(true);
        when(tagDao.create(tag)).thenReturn(tag);
        Optional<Tag> actual = giftCertificateService.createTagInGiftCertificate(1L, tag);
        assertEquals(Optional.of(tag), actual);
    }

    @Test
    void addTagToGiftCertificate() {
        when(giftCertificateDao.addTagToCertificate(1L, 1L)).thenReturn(true);
        when(giftCertificateDao.findById(1L)).thenReturn(Optional.of(new GiftCertificate()));
        when(tagDao.findById(1L)).thenReturn(Optional.of(new Tag()));
        boolean condition = giftCertificateService.addTagToGiftCertificate(1L, 1L);
        verify(giftCertificateDao, times(1)).addTagToCertificate(1L, 1L);
        assertTrue(condition);
    }

    @Test
    void deleteGiftCertificate() {
        when(giftCertificateDao.deleteById(1L)).thenReturn(true);
        boolean condition = giftCertificateService.deleteGiftCertificate(1L);
        verify(giftCertificateDao, times(1)).deleteById(1L);
        assertTrue(condition);
    }

    @Test
    void findGiftCertificateByTagName() {
        String name = "name";
        List<GiftCertificate> giftCertificates = new ArrayList<>();
        giftCertificates.add(new GiftCertificate());
        when(giftCertificateDao.countGiftCertificateByTagName(name)).thenReturn(10L);
        when(giftCertificateDao.findGiftCertificatesByTagNameWithOffsetAndLimit(name, 0, 5)).thenReturn(giftCertificates);
        Page<GiftCertificate> actual = giftCertificateService.findGiftCertificateByTagName(name, 1, 5);
        Page<GiftCertificate> expect = new Page<>(giftCertificates, 2, 10, 1, 5);
        assertEquals(expect, actual);
    }

    @Test
    void searchGiftCertificate() {
        String name = "name";
        List<GiftCertificate> giftCertificates = new ArrayList<>();
        giftCertificates.add(new GiftCertificate());
        when(giftCertificateDao.countGiftCertificateLikeNameOrDescription(name)).thenReturn(10L);
        when(giftCertificateDao.findGiftCertificateLikeNameOrDescription(name, 0, 5)).thenReturn(giftCertificates);
        Page<GiftCertificate> actual = giftCertificateService.searchGiftCertificate(name, 1, 5);
        Page<GiftCertificate> expect = new Page<>(giftCertificates, 2, 10, 1, 5);
        assertEquals(expect, actual);
    }
}