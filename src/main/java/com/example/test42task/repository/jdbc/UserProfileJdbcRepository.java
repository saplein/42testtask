package com.example.test42task.repository.jdbc;

import com.example.test42task.dto.UserWithProfileDto;
import com.example.test42task.repository.UserProfileRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class UserProfileJdbcRepository implements UserProfileRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserProfileJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<UserWithProfileDto> getUsersWithProfiles() {
        String sql = """
            SELECT 
                u.id as user_id,
                u.first_name,
                u.last_name,
                u.created_at as user_created,
                u.updated_at as user_updated,
                up.address,
                up.favorite_pet
            FROM users u
            LEFT JOIN user_profile up ON u.id = up.user_id
            """;

        return jdbcTemplate.query(sql, new UserWithProfileRowMapper());
    }

    private static class UserWithProfileRowMapper implements RowMapper<UserWithProfileDto> {
        @Override
        public UserWithProfileDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new UserWithProfileDto(
                    rs.getLong("user_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    convertToLocalDateTime(rs.getTimestamp("user_created")),
                    convertToLocalDateTime(rs.getTimestamp("user_updated")),
                    rs.getString("address"),
                    rs.getString("favorite_pet")
            );
        }

        private LocalDateTime convertToLocalDateTime(java.sql.Timestamp timestamp) {
            return timestamp != null ? timestamp.toLocalDateTime() : null;
        }
    }
}
