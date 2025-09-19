package com.example.test42task.service;

import com.example.test42task.dto.UserDto;
import com.example.test42task.dto.UserPatchRequest;

import java.util.List;
import java.util.Map;

public interface UserService {

    /**
     * Найти всех пользователей
     * @return список всех пользователей
     */
    List<UserDto> findAllUsers();

    /**
     * Найти пользователя по ID
     * @param id ID пользователя
     * @return найденный пользователь
     * @throws: если пользователь не найден
     */
    UserDto findUserById(Long id);

    /**
     * Сохранить нового пользователя
     * @param user объект пользователя для сохранения
     * @return сохраненный пользователь
     */
    UserDto saveUser(UserDto user);

    /**
     * Полное обновление пользователя (PUT)
     * @param id ID пользователя для обновления
     * @param user объект с новыми данными
     * @return обновленный пользователь
     * @throws: если пользователь не найден
     */
    UserDto fullUpdateUser(Long id, UserDto user);

    /**
     * Частичное обновление пользователя (PATCH)
     * @param id ID пользователя для обновления
     * @param updates Map с полями для обновления
     * @return обновленный пользователь
     * @throws: если пользователь не найден
     */
    void partialUpdateUser(Long id, UserPatchRequest updates);

    /**
     * Удалить пользователя по ID
     * @param id ID пользователя для удаления
     * @throws: если пользователь не найден
     */
    void deleteUserById(Long id);
}
