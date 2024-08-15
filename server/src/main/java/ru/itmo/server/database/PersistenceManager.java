package ru.itmo.server.database;

import lombok.Builder;
import lombok.NonNull;
import ru.itmo.common.entities.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PersistenceManager {
    private final Connection connection;

    /**
     * Конструктор PersistenceManager.
     *
     * @param connection Соединение с базой данных.
     */
    public PersistenceManager(Connection connection) {
        this.connection = connection;
    }

    public int add(User user, StudyGroup studyGroup) throws SQLException {
        String sql_req = """
        INSERT INTO Groups (name, coordinate_x, coordinate_y, studentsCount, form_of_education, semsetr, admin_name, birthday, weight, passportID, eyeColor, creator_id)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql_req, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, studyGroup.getName());
            stmt.setLong(2, (studyGroup.getCoordinates().getX()));
            stmt.setLong(3, studyGroup.getCoordinates().getY());
            stmt.setInt(4, (studyGroup.getStudentsCount()));
            stmt.setString(5, String.valueOf(studyGroup.getFormOfEducation()));
            stmt.setString(6, studyGroup.getSemesterEnum().toString());

            Person admin = studyGroup.getGroupAdmin();
            if (admin != null) {
                stmt.setString(7, (studyGroup.getGroupAdmin().getName()));
                stmt.setString(8, (studyGroup.getGroupAdmin().getBirthday().toString()));
                stmt.setLong(9, (studyGroup.getGroupAdmin().getWeight()));
                stmt.setString(10, (studyGroup.getGroupAdmin().getPassportID()));
                stmt.setString(11, (studyGroup.getGroupAdmin().getEyeColor().name()));
            } else {
                stmt.setNull(7, Types.VARCHAR);
                stmt.setNull(8, Types.VARCHAR);
                stmt.setNull(9, Types.INTEGER);
                stmt.setNull(10, Types.VARCHAR);
                stmt.setNull(11, Types.VARCHAR);
            }
            stmt.setInt(12, user.getId());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("Inserting groups failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Inserting groups failed, no ID obtained.");
                }
            }
        }
    }

    /**
     * Обновляет данные о группе в базе данных.
     *
     * @param user   Пользователь, создавший группу.
     * @param studyGroup Обновленные данные о группе.
     * @throws SQLException Если происходит ошибка SQL.
     */
    public void update(User user, StudyGroup studyGroup) throws SQLException {
        String sql_req = "UPDATE Groups SET name = ?, coordinate_x = ?, coordinate_y = ?, studentsCount = ?, form_of_education = ?, semsetr = ?, admin_name = ?, birthday = ?, weight = ?, passportID = ?, eyeColor = ? WHERE id=? AND creator_id=?";

        try (PreparedStatement stmt = connection.prepareStatement(sql_req)) {
            stmt.setString(1, studyGroup.getName());
            stmt.setLong(2, (studyGroup.getCoordinates().getX()));
            stmt.setLong(3, studyGroup.getCoordinates().getY());
            stmt.setInt(4, (studyGroup.getStudentsCount()));
            stmt.setString(5, String.valueOf(studyGroup.getFormOfEducation()));
            stmt.setString(6, studyGroup.getSemesterEnum().toString());

            Person admin = studyGroup.getGroupAdmin();
            if (admin != null) {
                stmt.setString(7, (studyGroup.getGroupAdmin().getName()));
                stmt.setString(8, (studyGroup.getGroupAdmin().getBirthday().toString()));
                stmt.setLong(9, (studyGroup.getGroupAdmin().getWeight()));
                stmt.setString(10, (studyGroup.getGroupAdmin().getPassportID()));
                stmt.setString(11, (studyGroup.getGroupAdmin().getEyeColor().name()));
            } else {
                stmt.setNull(7, Types.VARCHAR);
                stmt.setNull(8, Types.VARCHAR);
                stmt.setNull(9, Types.INTEGER);
                stmt.setNull(10, Types.VARCHAR);
                stmt.setNull(11, Types.VARCHAR);
            }

            stmt.setLong(12, studyGroup.getId());
            stmt.setInt(13, user.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Updating groups failed, no rows affected.");
            }
        }
    }

    /**
     * Удаляет все группы, созданные пользователем.
     *
     * @param user Пользователь, создавший группы.
     * @throws SQLException Если происходит ошибка SQL.
     */
    public void clear(User user) throws SQLException {
        String sql_req = "DELETE FROM Groups WHERE creator_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql_req)) {
            stmt.setInt(1, user.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * Удаляет группу по её ID, если её создал данный пользователь.
     *
     * @param user Пользователь, создавший группу.
     * @param id   ID группы для удаления.
     * @return Количество удаленных строк.
     * @throws SQLException Если происходит ошибка SQL.
     */
    //    Также можно юзать для удаления по индексу, просто чекнуть индекс у группы и удалить по нему

    public int remove(User user, int id) throws SQLException {
        String sql_req = "DELETE FROM Groups WHERE id=? AND creator_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql_req)) {
            stmt.setInt(1, id);
            stmt.setInt(2, user.getId());
            return stmt.executeUpdate();
        }
    }

    /**
     * Удаляет все группы, созданные пользователем, StudentsCount которых равен заданным значениям.
     *
     * @param user   Пользователь, создавший драконов.
     * @param studentsCount значение для удаления.
     * @return Количество удаленных строк.
     * @throws SQLException Если происходит ошибка SQL.
     */

    //    юзается для RemoveAllByStudentsCount
    public int remove_by_studentsCount(User user, int studentsCount) throws SQLException {
        String deleteDragonSQL = "DELETE FROM Groups WHERE studentsCount = ?  AND creator_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteDragonSQL)) {
            stmt.setInt(1, studentsCount);
            stmt.setInt(2, user.getId());
            return stmt.executeUpdate();
        }
    }

    /**
     * Удаляет все группы, созданные пользователем, StudentsCount которых больше заданного.
     *
     * @param user   Пользователь, создавший драконов.
     * @param studentsCount значение для удаления.
     * @return Количество удаленных строк.
     * @throws SQLException Если происходит ошибка SQL.
     */

//    юзается для removeGreater
    public int remove_be_studentsCount(User user, int studentsCount) throws SQLException {
        String deleteDragonSQL = "DELETE FROM Groups WHERE studentsCount > ?  AND creator_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteDragonSQL)) {
            stmt.setInt(1, studentsCount);
            stmt.setInt(2, user.getId());
            return stmt.executeUpdate();
        }
    }

    /**
     * Загружает всех драконов из базы данных.
     *
     * @return Список драконов.
     * @throws SQLException Если происходит ошибка SQL.
     */
    public List<StudyGroup> loadGroups() throws SQLException {
        List<StudyGroup> groups = new ArrayList<>();
        String selectDragonsSQL = "SELECT * FROM dragons";
        try (PreparedStatement stmt = connection.prepareStatement(selectDragonsSQL)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Long id = rs.getLong("id");
                    String name = rs.getString("name");
                    Coordinates coordinates = new Coordinates(rs.getLong("x"), rs.getInt("y"));
                    Date creationDate = rs.getDate("creation_date");
                    int studentsCount = rs.getInt("studentsCount");
                    FormOfEducation formOfEducation = FormOfEducation.valueOf(rs.getString("form_of_education"));
                    Semester semesterEnum = Semester.valueOf((rs.getString("semester")));
                    Person groupAdmin = null;
                    if (rs.getString("admin_name") != null) {
                        groupAdmin = new Person(rs.getString("admin_name"), rs.getDate("birthday").toLocalDate().atTime(00, 00), rs.getLong("weight"), rs.getString("passportID"), Color.valueOf(rs.getString("hair_color")));
                    }
                    int creator_id = rs.getInt("creator_id");
                    StudyGroup group = new StudyGroup(id, name, coordinates, creationDate, studentsCount, formOfEducation, semesterEnum, groupAdmin, creator_id);
                    groups.add(group);
//
//                    Там чутка надо доделать в StudyGroup (конструктор допилить, чтобы оно делалось, воть, да, сори, я просто тупенький, как сапожок и не смог это сделать
//                    в компенсацию вот вам паук Василий /╲/\╭[☉﹏☉]╮/\╱\
//
                }
            }
        }
        return groups;
    }
}