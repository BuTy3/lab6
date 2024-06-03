package ru.itmo.common.entities.forms;

import ru.itmo.common.entities.Coordinates;
import ru.itmo.common.exception.InvalidFormException;
import ru.itmo.common.exception.InvalidScriptInputException;
import ru.itmo.common.utility.InputParser;

public class CoordinatesForm extends Form<Coordinates> {
    private final InputParser inputParser = new InputParser();

    @Override
    public Coordinates build() throws InvalidScriptInputException, InvalidFormException {
        try {
            long x = inputParser.readLong("Введите координату X (макс: 964)");
            int y = inputParser.readInt("Введите координату Y (макс: 751)");

            Coordinates coordinates = Coordinates.builder()
                    .x(x)
                    .y(y)
                    .build();

            if (!coordinates.validate()) {
                throw new InvalidFormException("Невалидные данные для Coordinates");
            }

            return coordinates;
        } catch (Exception e) {
            throw new InvalidFormException("Ошибка при создании Coordinates: " + e.getMessage());
        }
    }
}
