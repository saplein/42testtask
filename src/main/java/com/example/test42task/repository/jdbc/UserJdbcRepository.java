package com.example.test42task.repository.jdbc;

import com.example.test42task.dto.UserDto;
import com.example.test42task.exeptions.UserNotFoundException;
import com.example.test42task.repository.UserRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class UserJdbcRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    private static final String INSERT_SQL = """
    INSERT INTO users (created_at, first_name, last_name, updated_at, is_send)
    VALUES (?, ?, ?, ?, ?)
    """;

    private static final String FULL_UPDATE_SQL = """
    UPDATE users 
    SET first_name = ?, 
        last_name = ?, 
        updated_at = ?
    WHERE id = ?
    """;

    private static final String DELETE_SQL = "DELETE FROM users WHERE id = ?";

    public UserJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class UserRowMapper implements RowMapper<UserDto> {
        @Override
        public UserDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new UserDto(
                    rs.getLong("id"),
                    convertToLocalDateTime(rs.getTimestamp("created_at")),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    convertToLocalDateTime(rs.getTimestamp("updated_at")),
                    rs.getBoolean("is_send")
            );
        }

        private LocalDateTime convertToLocalDateTime(java.sql.Timestamp timestamp) {
            return timestamp != null ? timestamp.toLocalDateTime() : null;
        }
    }

    @Override
    public List<UserDto> findUsers() {
            String sql = """
            SELECT * FROM users
            """;
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    @Override
    public UserDto findUserById(long id) {
        String sql = """
        SELECT * FROM users WHERE id = ? 
        """;
        try {
            return jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(id);
        }
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        LocalDateTime now = LocalDateTime.now();
        userDto.setUserCreated(now);
        userDto.setUserUpdated(now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
            ps.setTimestamp(1, Timestamp.valueOf(userDto.getUserCreated()));
            ps.setString(2, userDto.getFirstName());
            ps.setString(3, userDto.getLastName());
            ps.setTimestamp(4, Timestamp.valueOf(userDto.getUserUpdated()));
            ps.setBoolean(5, userDto.isSend());
            return ps;
        }, keyHolder);

        Long generatedId = keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;
        userDto.setUserId(generatedId);

        return userDto;
    }

    @Override
    public UserDto fullUpdateUser(Long id, UserDto userDto) {
        LocalDateTime now = LocalDateTime.now();

        int updatedRows = jdbcTemplate.update(FULL_UPDATE_SQL,
                userDto.getFirstName(),
                userDto.getLastName(),
                Timestamp.valueOf(now),
                id
        );

        if (updatedRows == 0) {
            throw new UserNotFoundException(id);
        }

        userDto.setUserUpdated(now);

        return userDto;
    }

    private String buildPartialUpdateSql(Long id, Map<String, Object> updates) {
        StringBuilder sql = new StringBuilder("UPDATE users SET ");

        sql.append("updated_at = ?");

        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String field = entry.getKey();

            String columnName = mapFieldToColumn(field);
            sql.append(", ").append(columnName).append(" = ?");
        }

        sql.append(" WHERE id = ?");

        return sql.toString();
    }

    private String mapFieldToColumn(String fieldName) {
        switch (fieldName) {
            case "firstName": return "first_name";
            case "lastName": return "last_name";
            default: return fieldName;
        }
    }

    @Override
    public void partialUpdateUser(Long id, Map<String, Object> updates) {
        if (updates == null || updates.isEmpty()) {
            return;
        }

        String sql = buildPartialUpdateSql(id, updates);
        Object[] params = buildParamsArray(id, updates);

        int updatedRows = jdbcTemplate.update(sql, params);

        if (updatedRows == 0) {
            throw new UserNotFoundException(id);
        }
    }

    private Object[] buildParamsArray(Long id, Map<String, Object> updates) {
        List<Object> params = new ArrayList<>();

        params.add(Timestamp.valueOf(LocalDateTime.now()));

        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            params.add(entry.getValue());
        }

        params.add(id);

        return params.toArray();
    }

    @Override
    public void deleteUserById(Long id) {
        int updatedRows = jdbcTemplate.update(DELETE_SQL, id);

        if (updatedRows == 0) {
            throw new UserNotFoundException(id);
        }
    }
}
