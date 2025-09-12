package com.example.test42task.service.impl;

import com.example.test42task.exeptions.IllegalFieldUpdateException;
import com.example.test42task.exeptions.UserNotFoundException;
import com.example.test42task.model.User;
import com.example.test42task.repository.UserRepository;
import com.example.test42task.service.UserService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private static final Set<String> ALLOWED_FIELDS = Set.of("firstName", "lastName");


    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User save(User user) {
        // Ensure default value for isSend on creation
        user.setSend(false);
        return userRepository.save(user);
    }

    @Override
    public User fullUpdate(Long id, User userDetails) {
        User existingUser = findById(id);

        existingUser.setFirstName(userDetails.getFirstName());
        existingUser.setLastName(userDetails.getLastName());
        return userRepository.save(existingUser);
    }

    @Override
    public User partialUpdate(Long id, Map<String, Object> updates) {
        User existingUser = findById(id);

        validateUpdateFields(updates);
        applyUpdates(existingUser, updates);

        return userRepository.save(existingUser);
    }

    private void validateUpdateFields(Map<String, Object> updates) {
        updates.keySet().forEach(field -> {
            if (!ALLOWED_FIELDS.contains(field)) {
                throw new IllegalFieldUpdateException("Field '"
                        + field +
                        "' is not allowed for update. Allowed fields: "
                        + ALLOWED_FIELDS);
            }
        });
    }

    private void applyUpdates(User user, Map<String, Object> updates) {
        updates.forEach((field, value) -> {
            try {
                switch (field) {
                    case "firstName", "lastName", "email" -> {
                        validateStringValue(field, value);
                        BeanUtils.setProperty(user, field, value);
                    }
                }
            } catch (Exception e) {
                throw new IllegalFieldUpdateException("Failed to update field '" + field + "': " + e.getMessage());
            }
        });
    }

    private void validateStringValue(String field, Object value) {
        if (!(value instanceof String)) {
            throw new IllegalFieldUpdateException("Field '" + field + "' must be a string");
        }
        if (((String) value).isBlank()) {
            throw new IllegalFieldUpdateException("Field '" + field + "' cannot be empty");
        }
    }

    @Override
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }
}

