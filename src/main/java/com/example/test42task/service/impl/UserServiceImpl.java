package com.example.test42task.service.impl;

import com.example.test42task.dto.UserDto;
import com.example.test42task.exeptions.IllegalFieldUpdateException;
import com.example.test42task.exeptions.UserNotFoundException;
import com.example.test42task.repository.UserRepository;
import com.example.test42task.service.UserService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findUsers();
    }

    @Override
    public UserDto findUserById(Long id) {
        return userRepository.findUserById(id);
    }

    @Override
    public UserDto saveUser(UserDto user) {
        user.setSend(false);
        return userRepository.saveUser(user);
    }

    @Override
    public UserDto fullUpdateUser(Long id, UserDto userDetails) {
        UserDto existingUser = findUserById(id);

        existingUser.setFirstName(userDetails.getFirstName());
        existingUser.setLastName(userDetails.getLastName());
        return userRepository.fullUpdateUser(id, existingUser);
    }

    @Override
    public void partialUpdateUser(Long id, Map<String, Object> updates) {
        UserDto existingUser = findUserById(id);

        userRepository.partialUpdateUser(id, updates);
    }


    @Override
    public void deleteUserById(Long id) {
        findUserById(id);
        userRepository.deleteUserById(id);
    }
}

