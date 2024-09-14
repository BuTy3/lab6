package ru.itmo.server.dao;

import java.sql.*;

import static ru.itmo.server.database.ConnectionManager.executeUpdate;
import static ru.itmo.server.database.ConnectionManager.getConnection;

public class UsersTable {
    /*
    Скрипт для создания таблицы, если её ещё нет
     */
    private static String create_table = """
        CREATE TABLE IF NOT EXISTS Users (
        id SERIAL PRIMARY KEY,
        name VARCHAR(255) UNIQUE NOT NULL,
        password TEXT NOT NULL,
        register_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        last_login TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        );""";

    /**
     * Конструктор класса UsersTable.
     */
    public UsersTable() {}

    /**
     * Создает таблицу пользователей в базе данных, если она не существует.
     * Выполняет SQL-запрос {@link #create_table} для создания таблицы.
     */
    public void create_table() {
        Connection connection = getConnection();
        executeUpdate(connection, create_table);
        System.out.println("Table Users created");
    }
}
