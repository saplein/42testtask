package com.example.test42task.service;

import com.example.test42task.model.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    /**
     * Найти всех пользователей
     * @return список всех пользователей
     */
    List<User> findAll();

    /**
     * Найти пользователя по ID
     * @param id ID пользователя
     * @return найденный пользователь
     * @throws: если пользователь не найден
     */
    User findById(Long id);

    /**
     * Сохранить нового пользователя
     * @param user объект пользователя для сохранения
     * @return сохраненный пользователь
     */
    User save(User user);

    /**
     * Полное обновление пользователя (PUT)
     * @param id ID пользователя для обновления
     * @param user объект с новыми данными
     * @return обновленный пользователь
     * @throws: если пользователь не найден
     */
    User fullUpdate(Long id, User user);

    /**
     * Частичное обновление пользователя (PATCH)
     * @param id ID пользователя для обновления
     * @param updates Map с полями для обновления
     * @return обновленный пользователь
     * @throws: если пользователь не найден
     */
    User partialUpdate(Long id, Map<String, Object> updates);

    /**
     * Удалить пользователя по ID
     * @param id ID пользователя для удаления
     * @throws: если пользователь не найден
     */
    void deleteById(Long id);
}
