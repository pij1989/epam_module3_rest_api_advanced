package com.epam.esm.model.dao;

import com.epam.esm.model.entity.Tag;

import java.util.List;

public interface TagDao extends BaseDao<Long, Tag> {
    List<Tag> findTagsWithLimitAndOffset(int offset, int limit);

    long countTag();
}
