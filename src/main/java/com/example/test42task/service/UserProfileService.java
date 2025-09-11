package com.example.test42task.service;

import com.example.test42task.dto.UserWithProfileDto;

import java.util.List;

public interface UserProfileService {
    List<UserWithProfileDto> getAllUsersWithProfiles();
}
