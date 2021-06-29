package com.epam.esm.model.dao;

import com.epam.esm.model.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDao extends BaseDao<Long, Tag> {
    List<Tag> findTagsWithLimitAndOffset(int offset, int limit);

    Optional<Tag> findMaxCountTagByUserId(Long userId);

    long countTag();
}
