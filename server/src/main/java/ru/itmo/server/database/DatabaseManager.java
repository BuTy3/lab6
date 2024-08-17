package ru.itmo.server.database;

import ru.itmo.server.dao.StudyGroupsTable;
import ru.itmo.server.dao.UsersTable;
import ru.itmo.server.main.Main;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Класс для управления базой данных, создания базы данных и необходимых таблиц.
 */
public class DatabaseManager {
    private static final UsersTable usersTable = new UsersTable();
    private static final StudyGroupsTable studyGroupsTable = new StudyGroupsTable();

    /**
     * Создает базу данных, если она еще не существует, и инициализирует таблицы.
     *
     * @return Соединение с базой данных.
     */
    public static Connection createDatabaseIfNotExists() {
        try (Connection connection = ConnectionManager.getConnection()) {
            if (connection != null) {
                boolean databaseExists = checkDatabaseExists(connection);
                if (!databaseExists) {
                    Main.logger.info("Database and tables created successfully.");
                } else {
                    Main.logger.info("Database already exists.");
                }
                createTables(connection);
                return connection;
            } else {
                Main.logger.error("Failed to establish connection to the database.");
                return connection;
            }
        } catch (SQLException e) {
            Main.logger.error("Error while creating database: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Проверяет, существует ли база данных.
     *
     * @param connection Соединение с базой данных.
     * @return True, если база данных существует, false в противном случае.
     * @throws SQLException Если происходит ошибка SQL.
     */
    private static boolean checkDatabaseExists(Connection connection) throws SQLException {
        return connection.getMetaData().getCatalogs().next();
    }

    /**
     * Создает необходимые таблицы, если они еще не существуют.
     *
     * @param connection Соединение с базой данных.
     */
    public static void createTables(Connection connection) {
        if (connection != null) {
            usersTable.create_table();
            studyGroupsTable.create_table();
            Main.logger.info("Tables created successfully (if not existed).");
        } else {
            Main.logger.error("Connection is null.");
        }
    }
}