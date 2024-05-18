package ru.itmo.entities;

import ru.itmo.utility.Element;
import ru.itmo.utility.Validatable;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Класс, представляющий собой учебную группу.
 */
public class StudyGroup extends Element implements Validatable, Serializable {
    private static long nextId = 0;
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int studentsCount; //Значение поля должно быть больше 0
    private FormOfEducation formOfEducation; //Поле может быть null
    private Semester semesterEnum; //Поле может быть null
    private Person groupAdmin; //Поле может быть null

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
    public StudyGroup(String name, Coordinates coordinates, int studentsCount, FormOfEducation formOfEducation, Semester semesterEnum, Person groupAdmin) {
        this.id = nextId++;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = new Date();
        this.studentsCount = studentsCount;
        this.formOfEducation = formOfEducation;
        this.semesterEnum = semesterEnum;
        this.groupAdmin = groupAdmin;
    }

    /**
     * Обновляет значение переменной nextId, которая используется для генерации уникальных идентификаторов групп.
     *
     * @param newNextID Новое значение для переменной nextId.
     */
    public static void updateNextID(long newNextID) {
        nextId = newNextID;
    }

    /**
     * Проверяет валидность данных учебной группы.
     *
     * @return true, если все данные учебной группы являются валидными, в противном случае - false.
     */
    @Override
    public boolean validate() {
        if (id == null || id <= 0) {
            return false;
        }
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
        return true;
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
     * Возвращает идентификатор учебной группы.
     *
     * @return Идентификатор учебной группы.
     */
    public int getId() {
        return Math.toIntExact(id);
    }

    /**
     * Возвращает количество студентов в учебной группе.
     *
     * @return Количество студентов в учебной группе.
     */
    public Integer getStudentsCount() {
        return studentsCount;
    }

    /**
     * Возвращает форму обучения учебной группы.
     *
     * @return Форма обучения учебной группы.
     */
    public FormOfEducation getFormOfEducation() {
        return formOfEducation;
    }

    /**
     * Возвращает следующий доступный идентификатор учебной группы.
     *
     * @return Следующий доступный идентификатор учебной группы.
     */
    public static long getNextId() {
        return nextId;
    }

    /**
     * Возвращает название учебной группы.
     *
     * @return Название учебной группы.
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает координаты учебной группы.
     *
     * @return Координаты учебной группы.
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Возвращает дату создания учебной группы.
     *
     * @return Дата создания учебной группы.
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Возвращает семестр обучения учебной группы.
     *
     * @return Семестр обучения учебной группы.
     */
    public Semester getSemesterEnum() {
        return semesterEnum;
    }

    /**
     * Возвращает администратора учебной группы.
     *
     * @return Администратор учебной группы.
     */
    public Person getGroupAdmin() {
        return groupAdmin;
    }

    /**
     * Сравнивает эту учебную группу с другой по их идентификаторам.
     *
     * @param studyGroup Учебная группа для сравнения.
     * @return Результат сравнения: отрицательное значение, если этот объект меньше переданного объекта;
     *         положительное значение, если этот объект больше переданного объекта;
     *         нуль, если объекты равны.
     */
    public int compareTo(StudyGroup studyGroup) {
        return (int) (this.id - studyGroup.getId());
    }

    /**
     * Сравнивает эту учебную группу с другим элементом. Данный метод реализован для удовлетворения интерфейса Comparable,
     * но сравнение не имеет смысла и всегда возвращает нуль.
     *
     * @param o Элемент для сравнения.
     * @return Всегда возвращает нуль.
     */
    @Override
    public int compareTo(Element o) {
        return 0;
    }

    /**
     * Проверяет, равны ли этот объект и переданный объект.
     *
     * @param o Объект для сравнения.
     * @return true, если объекты равны, иначе false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudyGroup that = (StudyGroup) o;
        return Objects.equals(id, that.id);
    }

    /**
     * Вычисляет хеш-код для этой учебной группы.
     *
     * @return hash для учебной группы.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, studentsCount, formOfEducation, semesterEnum, groupAdmin);
    }

    /**
     * Возвращает строковое представление учебной группы в формате JSON.
     *
     * @return Строковое представление учебной группы в формате JSON.
     */
    @Override
    public String toString() {
        return "StudyGroup{\"id\": " + id + ", " +
                "\"name\": \"" + name + "\", " +
                "\"creationDate\": \"" + creationDate + "\", " +
                "\"coordinates\": \"" + coordinates + "\", " +
                "\"creationDate\": \"" + creationDate + "\", " +
                "\"studentsCount\": \"" + studentsCount + "\", " +
                "\"formOfEducation\": " + (formOfEducation == null ? "null" : "\"" + formOfEducation + "\"") + ", " +
                "\"semesterEnum\": " + (semesterEnum == null ? "null" : "\"" + semesterEnum + "\"") + ", " +
                "\"groupAdmin\": " + (groupAdmin == null ? "null" : "\"" + groupAdmin + "\"") + "\", " + "}";
    }
}
