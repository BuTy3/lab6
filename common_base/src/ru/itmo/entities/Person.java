package ru.itmo.entities;

import ru.itmo.utility.Validatable;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Класс, представляющий собой человека.
 */
public class Person implements Validatable, Comparable<Person>, Serializable {
    private String name; // Поле не может быть null, Строка не может быть пустой
    private LocalDateTime birthday; // Поле может быть null
    private long weight; // Значение поля должно быть больше 0
    private String passportID; // Строка не может быть пустой, Длина строки не должна быть больше 24, Поле не может быть null
    private Color eyeColor; // Поле не может быть null

    /**
     * Конструктор для создания экземпляра класса Person.
     *
     * @param name       Имя человека.
     * @param birthday   Дата рождения человека.
     * @param weight     Вес человека.
     * @param passportID Идентификационный номер паспорта человека.
     * @param eyeColor   Цвет глаз человека.
     */
    public Person(String name, LocalDateTime birthday, long weight, String passportID, Color eyeColor) {
        this.name = name;
        this.birthday = birthday;
        this.weight = weight;
        this.passportID = passportID;
        this.eyeColor = eyeColor;
    }

    /**
     * Возвращает строковое представление объекта Person.
     *
     * @return Строковое представление объекта.
     */
    @Override
    public String toString() {
        return "Person{\"name\": " + name + ", " +
                "\"birthday\": \"" + birthday + "\", " +
                "\"weight\": \"" + weight + "\", " +
                "\"passportID\": \"" + passportID + "\", " +
                "\"eyeColor\": " + eyeColor +  "\" " + "}";
    }

    /**
     * Проверяет валидность данных о человеке.
     *
     * @return true, если данные о человеке валидны, иначе false.
     */
    @Override
    public boolean validate() {
        if (name == null || name.isEmpty()) {
            return false;
        }
        if (passportID == null || passportID.isEmpty() || passportID.length() > 24) {
            return false;
        }
        if (weight <= 0) {
            return false;
        }
        if (eyeColor == null) {
            return false;
        }
        return true;
    }

    private CharSequence getName() {
        return name;
    }

    @Override
    public int compareTo(Person o) {
        return 0;
    }
}

