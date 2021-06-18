package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDaoImpl implements TagDao {
    private static final String CREATE_TAG_SQL = "INSERT INTO tag (name) VALUES (?)";
    private static final String FIND_TAG_BY_ID_SQL = "SELECT id,name FROM tag WHERE id = ?";
    private static final String FIND_ALL_TAG_SQL = "SELECT id,name FROM tag";
    private static final String DELETE_TAG_BY_ID_SQL = "DELETE FROM tag WHERE id = ?";
    private JdbcTemplate jdbcTemplate;
    private final RowMapper<Tag> rowMapper = (resultSet, rowNum) -> createTagFromResultSet(resultSet);

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Tag create(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TAG_SQL, new String[]{ColumnName.ID});
            preparedStatement.setString(1, tag.getName());
            return preparedStatement;
        }, keyHolder);
        Long id = (Long) keyHolder.getKey();
        tag.setId(id);
        return tag;
    }

    @Override
    public Optional<Tag> findById(Long id) {
        return jdbcTemplate.query(FIND_TAG_BY_ID_SQL,
                resultSet -> {
                    if (resultSet.next()) {
                        Tag tag = createTagFromResultSet(resultSet);
                        return Optional.of(tag);
                    }
                    return Optional.empty();
                },
                id);
//        Tag tag = jdbcTemplate.queryForObject(FIND_TAG_BY_ID_SQL, rowMapper, id);
//        return Optional.ofNullable(foundTag);
    }

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query(FIND_ALL_TAG_SQL, rowMapper);
    }

    @Override
    public Optional<Tag> update(Tag entity) {
        throw new UnsupportedOperationException("Unsupported operation 'update' for TagDao");
    }

    @Override
    public boolean deleteById(Long id) {
        int result = jdbcTemplate.update(DELETE_TAG_BY_ID_SQL, id);
        return result > 0;
    }

    private Tag createTagFromResultSet(ResultSet resultSet) throws SQLException {
        Tag tag = new Tag();
        tag.setId(resultSet.getLong(ColumnName.ID));
        tag.setName(resultSet.getString(ColumnName.NAME));
        return tag;
    }
}
