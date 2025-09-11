package com.example.test42task.repository;

import com.example.test42task.dto.UserWithProfileDto;

import java.util.List;

public interface UserProfileRepository {
    List<UserWithProfileDto> getUsersWithProfiles();
}
