package com.example.test42task.service;

import com.example.test42task.exeptions.IllegalFieldUpdateException;
import com.example.test42task.exeptions.ResourceNotFoundException;
import com.example.test42task.model.User;
import com.example.test42task.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private static final Set<String> ALLOWED_FIELDS = Set.of("firstName", "lastName");


    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public User save(User user) {
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
                throw new IllegalFieldUpdateException("Field '" + field + "' is not allowed for update. Allowed fields: " + ALLOWED_FIELDS);
            }
        });
    }

    private void applyUpdates(User user, Map<String, Object> updates) {
        updates.forEach((field, value) -> {
            switch (field) {
                case "firstName" -> {
                    validateStringValue(field, value);
                    user.setFirstName((String) value);
                }
                case "lastName" -> {
                    validateStringValue(field, value);
                    user.setLastName((String) value);
                }
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
        User user = findById(id);

        userRepository.deleteById(id);
    }
}
