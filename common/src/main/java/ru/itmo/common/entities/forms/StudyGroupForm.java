package ru.itmo.common.entities.forms;

import ru.itmo.common.entities.*;
import ru.itmo.common.exception.InvalidFormException;
import ru.itmo.common.exception.InvalidScriptInputException;
import ru.itmo.common.utility.InputParser;

public class StudyGroupForm extends Form<StudyGroup> {

    @Override
    public StudyGroup build() throws InvalidScriptInputException, InvalidFormException {
        InputParser inputParser = new InputParser();
        try {
            String name = inputParser.readString("Введите название группы");
            Coordinates coordinates = new CoordinatesForm().build(); // Предположим, что есть класс CoordinatesForm
            int studentsCount = inputParser.readInt("Введите количество студентов в группе");
            FormOfEducation formOfEducation = inputParser.readFormOfEducation("Введите форму обучения");
            Semester semesterEnum = inputParser.readSemester("Введите семестр");
            Person groupAdmin = new PersonForm().build(); // Предположим, что есть класс PersonForm

            return StudyGroup.builder()
                    .name(name)
                    .coordinates(coordinates)
                    .studentsCount(studentsCount)
                    .formOfEducation(formOfEducation)
                    .semesterEnum(semesterEnum)
                    .groupAdmin(groupAdmin)
                    .build();

        } catch (Exception e) {
            throw new InvalidFormException("Ошибка при создании StudyGroup: " + e.getMessage());
        }
    }
}
