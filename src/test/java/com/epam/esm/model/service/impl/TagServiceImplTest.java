package com.epam.esm.model.service.impl;

import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.entity.Tag;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class TagServiceImplTest {
    private Tag tag;

    @Mock
    private TagDao tagDao;

    @InjectMocks
    private TagServiceImpl tagService;

    @BeforeEach
    void setUp() {
        tag = new Tag();
        tag.setId(1L);
        tag.setName("New tag name");
    }

    @AfterEach
    void tearDown() {
        tag = null;
    }

    @Test
    void create() {
        when(tagDao.create(tag)).thenReturn(tag);
        Optional<Tag> actual = tagService.create(tag);
        verify(tagDao, times(1)).create(tag);
        assertEquals(Optional.of(tag), actual);
    }

    @Test
    void findTag() {
        Long id = 1L;
        when(tagDao.findById(id)).thenReturn(Optional.of(tag));
        Optional<Tag> actual = tagService.findTag(id);
        assertEquals(Optional.of(tag), actual);
    }

    @Test
    void findAllTag() {
        when(tagDao.findAll()).thenReturn(List.of(new Tag(), new Tag()));
        List<Tag> tags = tagService.findAllTag();
        verify(tagDao,times(1)).findAll();
        assertEquals(2, tags.size());
    }

    @Test
    void deleteTag() {
        Long id = 1L;
        when(tagDao.deleteById(id)).thenReturn(true);
        boolean condition = tagService.deleteTag(id);
        verify(tagDao,times(1)).deleteById(id);
        assertTrue(condition);
    }
}