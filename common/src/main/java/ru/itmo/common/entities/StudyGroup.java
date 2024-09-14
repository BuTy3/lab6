package ru.itmo.common.entities;

import lombok.*;
import ru.itmo.common.utility.Element;
import ru.itmo.common.utility.Validatable;

import java.io.Serializable;
import java.util.Date;

/**
 * Класс, представляющий собой учебную группу.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false, exclude = {"id"})
@AllArgsConstructor
@Builder
public class StudyGroup extends Element<StudyGroup> implements Validatable, Comparable<StudyGroup>, Serializable {
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    @Builder.Default
    private Date creationDate = new Date(); //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int studentsCount; //Значение поля должно быть больше 0
    private FormOfEducation formOfEducation; //Поле может быть null
    private Semester semesterEnum; //Поле может быть null
    private Person groupAdmin; //Поле может быть null
    private String username;

    /**
     * Создает новый объект StudyGroup с указанными параметрами.
     *
     * @param name            Название группы.
     * @param coordinates     Координаты местоположения группы.
     * @param studentsCount   Количество студентов в группе.
     * @param formOfEducation Форма обучения группы.
     * @param semesterEnum    Семестр обучения группы.
     * @param groupAdmin      Администратор группы.
     */
    public StudyGroup(@NonNull String name, @NonNull Coordinates coordinates, int studentsCount,
                      FormOfEducation formOfEducation, Semester semesterEnum, Person groupAdmin) {
        this.name = name;
        this.coordinates = coordinates;
        this.studentsCount = studentsCount;
        this.formOfEducation = formOfEducation;
        this.semesterEnum = semesterEnum;
        this.groupAdmin = groupAdmin;
    }

    public StudyGroup(Long id, String name, Coordinates coordinates, Date creationDate, int studentsCount, FormOfEducation formOfEducation, Semester semesterEnum, Person groupAdmin, int creatorId) {
        super();
//        тут чутка доделать надо (*^.^*)
    }

    /**
     * Проверяет валидность данных учебной группы.
     *
     * @return true, если все данные учебной группы являются валидными, в противном случае - false.
     */
    @Override
    public boolean validate() {
        if (name == null || name.isEmpty()) {
            return false;
        }
        if (coordinates == null) {
            return false;
        }
        if (creationDate == null) {
            return false;
        }
        if (studentsCount <= 0) {
            return false;
        }
        if (groupAdmin != null && !groupAdmin.validate()) {
            return false;
        }
        return coordinates != null && coordinates.validate();
    }

    /**
     * Обновляет данные учебной группы на основе данных другой группы.
     *
     * @param studyGroup Объект StudyGroup, содержащий обновленные данные.
     */
    public void update(StudyGroup studyGroup) {
        this.name = studyGroup.name;
        this.coordinates = studyGroup.coordinates;
        this.creationDate = studyGroup.creationDate;
        this.studentsCount = studyGroup.studentsCount;
        this.formOfEducation = studyGroup.formOfEducation;
        this.semesterEnum = studyGroup.semesterEnum;
        this.groupAdmin = studyGroup.groupAdmin;
    }

    /**
     * Сравнивает эту учебную группу с другой по их количеству студентов.
     *
     * @param studyGroup Учебная группа для сравнения.
     * @return Результат сравнения: отрицательное значение, если этот объект меньше переданного объекта;
     * положительное значение, если этот объект больше переданного объекта;
     * нуль, если объекты равны.
     */
    @Override
    public int compareTo(StudyGroup studyGroup) {
        return Long.compare(this.studentsCount, studyGroup.getStudentsCount());
    }
}
