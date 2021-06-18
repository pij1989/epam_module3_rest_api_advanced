package com.epam.esm.model.dao.impl;

import com.epam.esm.model.dao.GiftCertificateDao;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {
    private static final String PERCENT = "%";
    private static final String CREATE_GIFT_CERTIFICATE_SQL = "INSERT INTO gift_certificate(name,description,price,duration,create_date,last_update_date) VALUES (?,?,?,?,?,?)";
    private static final String FIND_GIFT_CERTIFICATE_BY_ID_SQL = "SELECT id,name,description,price,duration,create_date,last_update_date FROM gift_certificate WHERE id = ?";
    private static final String FIND_GIFT_CERTIFICATE_TAG_BY_GIFT_CERTIFICATE_ID_SQL = "SELECT t.id AS tag_id,t.name AS tag_name FROM gift_certificate_tag AS gct JOIN tag AS t ON gct.tag_id = t.id WHERE gct.gift_certificate_id = ?";
    private static final String FIND_GIFT_CERTIFICATE_TAG_BY_TAG_ID_SQL = "SELECT gc.id,gc.name,gc.description,gc.price,gc.duration,gc.create_date,gc.last_update_date FROM gift_certificate_tag AS gct JOIN gift_certificate AS gc ON gct.gift_certificate_id = gc.id WHERE gct.tag_id = ?";
    private static final String FIND_ALL_GIFT_CERTIFICATE_SQL = "SELECT id,name,description,price,duration,create_date,last_update_date FROM gift_certificate ORDER BY id";
    private static final String DELETE_GIFT_CERTIFICATE_SQL = "DELETE FROM gift_certificate WHERE id = ?";
    private static final String ADD_TAG_TO_GIFT_CERTIFICATE_SQL = "INSERT INTO gift_certificate_tag(gift_certificate_id, tag_id) VALUES (?,?)";
    private static final String UPDATE_GIFT_CERTIFICATE_SQL = "UPDATE gift_certificate SET name = ?, description = ?,price = ?,duration = ?,create_date = ?,last_update_date = ? WHERE id = ?";
    private static final String FIND_GIFT_CERTIFICATE_BY_TAG_NAME_SQL = "SELECT gc.id,gc.name,gc.description,gc.price,gc.duration,gc.create_date,gc.last_update_date FROM gift_certificate AS gc JOIN gift_certificate_tag ON gc.id = gift_certificate_id JOIN tag AS t ON t.id = tag_id WHERE t.name = ?";
    private static final String FIND_GIFT_CERTIFICATE_LIKE_NAME_OR_DESCRIPTION = "SELECT id,name,description,price,duration,create_date,last_update_date FROM gift_certificate WHERE (name ILIKE ? OR description ILIKE ?)";
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public GiftCertificate create(GiftCertificate giftCertificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_GIFT_CERTIFICATE_SQL, new String[]{ColumnName.ID});
            preparedStatement.setString(1, giftCertificate.getName());
            preparedStatement.setString(2, giftCertificate.getDescription());
            preparedStatement.setBigDecimal(3, giftCertificate.getPrice());
            preparedStatement.setInt(4, giftCertificate.getDuration());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(giftCertificate.getCreateDate()));
            preparedStatement.setTimestamp(6, Timestamp.valueOf(giftCertificate.getLastUpdateDate()));
            return preparedStatement;
        }, keyHolder);
        Long id = (Long) keyHolder.getKey();
        giftCertificate.setId(id);
        return giftCertificate;
    }

    @Override
    public Optional<GiftCertificate> findById(Long id) {
        return jdbcTemplate.query(FIND_GIFT_CERTIFICATE_BY_ID_SQL, rs -> {
            if (rs.next()) {
                GiftCertificate giftCertificate = createGiftCertificateWithoutTagsFromResultSet(rs);
                addTagsToGiftCertificate(giftCertificate, id);
                return Optional.of(giftCertificate);
            }
            return Optional.empty();
        }, id);
    }

    @Override
    public List<GiftCertificate> findAll() {
        List<GiftCertificate> giftCertificates = jdbcTemplate.query(FIND_ALL_GIFT_CERTIFICATE_SQL,
                (rs, rowNum) -> createGiftCertificateWithoutTagsFromResultSet(rs));
        return giftCertificates.stream()
                .peek(giftCertificate -> {
                    Long id = giftCertificate.getId();
                    addTagsToGiftCertificate(giftCertificate, id);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<GiftCertificate> update(GiftCertificate giftCertificate) {
        int result = jdbcTemplate.update(UPDATE_GIFT_CERTIFICATE_SQL, giftCertificate.getName(), giftCertificate.getDescription(),
                giftCertificate.getPrice(), giftCertificate.getDuration(), giftCertificate.getCreateDate(),
                giftCertificate.getLastUpdateDate(), giftCertificate.getId());
        return result > 0 ? Optional.of(giftCertificate) : Optional.empty();
    }

    @Override
    public boolean deleteById(Long id) {
        int result = jdbcTemplate.update(DELETE_GIFT_CERTIFICATE_SQL, id);
        return result > 0;
    }

    @Override
    public boolean addTagToCertificate(Long certificateId, Long tagId) {
        int result = jdbcTemplate.update(ADD_TAG_TO_GIFT_CERTIFICATE_SQL, certificateId, tagId);
        return result > 0;
    }

    @Override
    public Optional<GiftCertificate> updatePart(GiftCertificate giftCertificate) {
        return jdbcTemplate.query(connection -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(FIND_GIFT_CERTIFICATE_BY_ID_SQL,
                            ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    preparedStatement.setLong(1, giftCertificate.getId());
                    return preparedStatement;
                },
                rs -> {
                    if (rs.next()) {
                        if (giftCertificate.getName() != null) {
                            rs.updateString(ColumnName.NAME, giftCertificate.getName());
                        }
                        if (giftCertificate.getDescription() != null) {
                            rs.updateString(ColumnName.DESCRIPTION, giftCertificate.getDescription());
                        }
                        if (giftCertificate.getPrice() != null) {
                            rs.updateBigDecimal(ColumnName.PRICE, giftCertificate.getPrice());
                        }
                        if (giftCertificate.getDuration() != null) {
                            rs.updateInt(ColumnName.DURATION, giftCertificate.getDuration());
                        }
                        if (giftCertificate.getCreateDate() != null) {
                            rs.updateTimestamp(ColumnName.CREATE_DATE, Timestamp.valueOf(giftCertificate.getCreateDate()));
                        }
                        if (giftCertificate.getLastUpdateDate() != null) {
                            rs.updateTimestamp(ColumnName.LAST_UPDATE_DATE, Timestamp.valueOf(giftCertificate.getLastUpdateDate()));
                        }
                        rs.updateRow();
                        return Optional.of(createGiftCertificateWithoutTagsFromResultSet(rs));
                    }
                    return Optional.empty();
                });
    }

    @Override
    public List<GiftCertificate> findGiftCertificatesByTagName(String name) {
        List<GiftCertificate> giftCertificates = jdbcTemplate.query(FIND_GIFT_CERTIFICATE_BY_TAG_NAME_SQL,
                (rs, rowNum) -> createGiftCertificateWithoutTagsFromResultSet(rs), name);
        return giftCertificates.stream()
                .peek(giftCertificate -> {
                    Long id = giftCertificate.getId();
                    addTagsToGiftCertificate(giftCertificate, id);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<GiftCertificate> findGiftCertificateLikeNameOrDescription(String filter) {
        String filterWithPercent = PERCENT + filter + PERCENT;
        List<GiftCertificate> giftCertificates = jdbcTemplate.query(FIND_GIFT_CERTIFICATE_LIKE_NAME_OR_DESCRIPTION,
                (rs, rowNum) -> createGiftCertificateWithoutTagsFromResultSet(rs), filterWithPercent, filterWithPercent);
        return giftCertificates.stream()
                .peek(giftCertificate -> {
                    Long id = giftCertificate.getId();
                    addTagsToGiftCertificate(giftCertificate, id);
                })
                .collect(Collectors.toList());
    }

    private GiftCertificate createGiftCertificateWithoutTagsFromResultSet(ResultSet rs) throws SQLException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(rs.getLong(ColumnName.ID));
        giftCertificate.setName(rs.getString(ColumnName.NAME));
        giftCertificate.setDescription(rs.getString(ColumnName.DESCRIPTION));
        giftCertificate.setPrice(rs.getBigDecimal(ColumnName.PRICE));
        giftCertificate.setDuration(rs.getInt(ColumnName.DURATION));
        giftCertificate.setCreateDate(rs.getTimestamp(ColumnName.CREATE_DATE).toLocalDateTime());
        giftCertificate.setLastUpdateDate(rs.getTimestamp(ColumnName.LAST_UPDATE_DATE).toLocalDateTime());
        return giftCertificate;
    }

    private Tag createTagFromResultSet(ResultSet rs) throws SQLException {
        Tag tag = new Tag();
        tag.setId(rs.getLong(ColumnName.TAG_ID));
        tag.setName(rs.getString(ColumnName.TAG_NAME));
        return tag;
    }

    private void addTagsToGiftCertificate(GiftCertificate giftCertificate, Long id) {
        List<Tag> tags = jdbcTemplate.query(FIND_GIFT_CERTIFICATE_TAG_BY_GIFT_CERTIFICATE_ID_SQL,
                (resultSet, rowNum) -> createTagFromResultSet(resultSet), id);
        tags = tags.stream()
                .peek(tag -> {
                    Long tagId = tag.getId();
                    List<GiftCertificate> giftCertificates = jdbcTemplate.query(FIND_GIFT_CERTIFICATE_TAG_BY_TAG_ID_SQL,
                            (resultSet, rowNum) -> createGiftCertificateWithoutTagsFromResultSet(resultSet), tagId);
                    tag.setGiftCertificates(giftCertificates);
                })
                .collect(Collectors.toList());
        giftCertificate.setTags(tags);
    }
}
