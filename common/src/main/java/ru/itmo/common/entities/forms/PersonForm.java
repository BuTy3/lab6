package ru.itmo.common.entities.forms;

import ru.itmo.common.entities.Color;
import ru.itmo.common.entities.Person;
import ru.itmo.common.exception.InvalidFormException;
import ru.itmo.common.exception.InvalidScriptInputException;
import ru.itmo.common.utility.InputParser;

import java.time.LocalDateTime;

public class PersonForm extends Form<Person> {
    private final InputParser inputParser = new InputParser();

    @Override
    public Person build() throws InvalidScriptInputException, InvalidFormException {
        try {
            String name = inputParser.readString("Введите имя");
            LocalDateTime birthday = inputParser.readLocalDateTime("Введите дату рождения (формат: YYYY-MM-DDTHH:MM:SS)");
            long weight = inputParser.readLong("Введите вес");
            String passportID = inputParser.readString("Введите паспортный ID");
            Color eyeColor = inputParser.readColor("Введите цвет глаз");

            Person person = Person.builder()
                    .name(name)
                    .birthday(birthday)
                    .weight(weight)
                    .passportID(passportID)
                    .eyeColor(eyeColor)
                    .build();

            if (!person.validate()) {
                throw new InvalidFormException("Невалидные данные для Person");
            }

            return person;
        } catch (Exception e) {
            throw new InvalidFormException("Ошибка при создании Person: " + e.getMessage());
        }
    }
}
