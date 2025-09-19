package com.example.test42task.repository;

import com.example.test42task.dto.UserDto;
import com.example.test42task.dto.UserPatchRequest;

import java.util.List;
import java.util.Map;

public interface UserRepository {
    List<UserDto> findUsers();

    UserDto findUserById(long id);

    UserDto saveUser(UserDto user);

    UserDto fullUpdateUser(Long id,UserDto user);

    void partialUpdateUser(Long id, UserPatchRequest updates);

    void deleteUserById(Long id);
}
