package ru.itmo.common.utility;

import ru.itmo.common.entities.User;

import java.sql.Connection;
import java.sql.SQLException;

public interface UserRepository {
    /**
     * Метод для получения пользователя по имени с использованием объекта Connection.
     *
     * @param username имя пользователя.
     * @return объект User, если пользователь найден, иначе null.
     * @throws SQLException в случае ошибки работы с базой данных.
     */
    User getUserByUsername(String username) throws SQLException;

    public User insertUser(String username, String password) throws SQLException;
}
