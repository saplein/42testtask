package com.example.test42task.service.impl;

import com.example.test42task.dto.UserWithProfileDto;
import com.example.test42task.repository.UserProfileRepository;
import com.example.test42task.service.UserProfileService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileServiceImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public List<UserWithProfileDto> getAllUsersWithProfiles() {
        return userProfileRepository.getUsersWithProfiles();
    }
}
