package it.MuseoSmart.dao;

import it.MuseoSmart.dto.PaintingDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PaintingDAOImpl implements PaintingDAO {

    private final JdbcTemplate jdbcTemplate;

    public PaintingDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<PaintingDTO> paintingRowMapper = (rs, rowNum) -> PaintingDTO.builder()
            .id(rs.getLong("id"))
            .museumName(rs.getString("museum_name"))
            .paintingName(rs.getString("painting_name"))
            .authorName(rs.getString("author_name"))
            .description(rs.getString("description"))
            .imgUrl(rs.getString("img_url"))
            .build();

    @Override
    public PaintingDTO getPaintingById(Long id) {
        String sql = "SELECT * FROM paintings WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, paintingRowMapper, id);
    }
}
