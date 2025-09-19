package com.example.test42task.repository.jdbc;

import com.example.test42task.dto.UserDto;
import com.example.test42task.dto.UserPatchRequest;
import com.example.test42task.exeptions.GlobalExceptionHandler;
import com.example.test42task.exeptions.IllegalFieldUpdateException;
import com.example.test42task.exeptions.ResourceNotFoundException;
import com.example.test42task.exeptions.UserNotFoundException;
import com.example.test42task.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Repository
public class UserJdbcRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    private final GlobalExceptionHandler globalExceptionHandler;

    private static final String INSERT_SQL = """
    
            INSERT INTO users (created_at, first_name, last_name, updated_at, is_send)
    VALUES (?, ?, ?, ?, ?)
    """;

    private static final String FULL_UPDATE_SQL =
            """
    UPDATE users 
    SET first_name = ?,
                 last_name = ?,
                 updated_at = ?
             WHERE id = ?
             """;

    private static final String DELETE_SQL = "DELETE FROM users WHERE id = ?";

    public UserJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.globalExceptionHandler = new GlobalExceptionHandler();
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
        String sql = "SELECT id, created_at, first_name, last_name, updated_at, is_send FROM users";
        log.debug("Executing SQL: {}", sql);

        List<UserDto> users = jdbcTemplate.query(sql, new UserRowMapper());
        log.info("Found {} users", users.size());

        return users;
    }

    @Override
    public UserDto findUserById(long id) {
        String sql = "SELECT id, created_at, first_name, last_name, updated_at, is_send FROM users WHERE id = ?";
        log.debug("Executing SQL: {} with id={}", sql, id);

        try {
            UserDto user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
            log.info("User with id={} found: {}", id, user);
            return user;
        } catch (EmptyResultDataAccessException e) {
            log.warn("User with id={} not found", id);
            throw new UserNotFoundException(id);
        }
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        log.debug("Saving new user: {}", userDto);

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

        log.info("User saved with id={}", generatedId);

        return userDto;
    }

    @Override
    public UserDto fullUpdateUser(Long id, UserDto userDto) {
        log.debug("Calling fullUpdateUser with id={} and userDto={}", id, userDto);

        if(userDto.getFirstName() != null || userDto.getLastName() != null ){
            log.error("Invalid put request for user id={}", id);
            throw new IllegalFieldUpdateException(
                    "Отсутствуют входные данные для обновления или названия параметров указаны в неверном формате", id);
        }

        LocalDateTime now = LocalDateTime.now();

        log.debug("Executing SQL: {} with params: firstName={}, lastName={}, updatedAt={}, id={}",
                FULL_UPDATE_SQL, userDto.getFirstName(), userDto.getLastName(), now, id);

        int updatedRows = jdbcTemplate.update(
                FULL_UPDATE_SQL,
                userDto.getFirstName(),
                userDto.getLastName(),
                Timestamp.valueOf(now),
                id
        );

        if (updatedRows == 0) {
            log.warn("User with id={} not found for full update", id);
            throw new UserNotFoundException(id);
        }

        userDto.setUserUpdated(now);

        log.info("User with id={} successfully updated at {}", id, now);

        return userDto;
    }

    @Override
    public void partialUpdateUser(Long id, UserPatchRequest userUpdates) {
        log.debug("Partially updating user with id={} and updates={}", id, userUpdates);

        if(userUpdates.getFirstName() == null && userUpdates.getLastName() == null ){
            log.error("Invalid patch request for user id={}", id);
            throw new IllegalFieldUpdateException(
                    "Отсутствуют входные данные для обновления или названия параметров указаны в неверном формате", id);
        }

        if(findUserById(id) == null ){
            log.error("User with id={} not found for patch", id);
            throw new UserNotFoundException(id);
        }

        List<String> updates = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        if (userUpdates.getFirstName() != null) {
            updates.add("first_name = ?");
            params.add(userUpdates.getFirstName());
        }

        if (userUpdates.getLastName() != null) {
            updates.add("last_name = ?");
            params.add(userUpdates.getLastName());
        }

        String sql = "UPDATE users SET updated_at = ?, " + String.join(", ", updates) + " WHERE id = ?";
        params.add(0, Timestamp.valueOf(LocalDateTime.now()));
        params.add(id);

        int updatedRows = jdbcTemplate.update(sql, params.toArray());

        log.info("Patched user id={}, updatedRows={}", id, updatedRows);
    }

    @Override
    public void deleteUserById(Long id) {
        log.debug("Deleting user with id={}", id);

        int updatedRows = jdbcTemplate.update(DELETE_SQL, id);

        if (updatedRows == 0) {
            log.warn("Attempt to delete user with id={} failed: not found", id);
            throw new UserNotFoundException(id);
        }

        log.info("Deleted user with id={}", id);
    }
}