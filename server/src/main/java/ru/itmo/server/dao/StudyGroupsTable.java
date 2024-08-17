package ru.itmo.server.dao;

import java.io.Serializable;
import java.sql.Connection;

import static ru.itmo.server.database.ConnectionManager.executeUpdate;
import static ru.itmo.server.database.ConnectionManager.getConnection;

public class StudyGroupsTable implements Serializable {


    /*
    Скрипт для создания таблицы, если её ещё нет
     */
    private static String create_table = """
        CREATE TABLE IF NOT EXISTS Groups (
        id SERIAL PRIMARY KEY,
        name VARCHAR(255), 
        coordinate_x SMALLINT, 
        coordinate_y SMALLINT NOT NULL, 
        creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
        studentsCount INT, 
        form_of_education VARCHAR(50), 
        semester VARCHAR(50),
        admin_name VARCHAR(255),
        birthday TIMESTAMP,
        weight INTEGER,
        passportID VARCHAR(24),
        eyeColor VARCHAR(50),
        creator_id INT REFERENCES Users (id)
        );""";

    /**
     * Конструктор класса StudyGroupsTable.
     */
    public StudyGroupsTable() {}

    /**
     * Создает таблицу групп в базе данных, если она не существует.
     * Выполняет SQL-запрос {@link #create_table} для создания таблицы.
     */
    public void create_table() {
        Connection connection = getConnection();
        executeUpdate(connection, create_table);
        System.out.println("Table Groups created");
    }


}
