package ru.itmo.commands;

import ru.itmo.network.Answer;
import ru.itmo.network.Request;

import java.util.Objects;

/**
 * Абстрактный класс Command представляет команду, которая может быть выполнена.
 * Реализует интерфейсы Description и Execute для получения описания и выполнения команды соответственно.
 */
public abstract class Command implements Description, Execute {
    /**
     * Название команды.
     */
    private final String name;
    /**
     * Описание команды.
     */
    private final String description;

    /**
     * Конструктор для создания команды с заданным именем и описанием.
     *
     * @param name        Название команды.
     * @param description Описание команды.
     */
    public Command(CommandName name, String description) {
        this.name = name.toString().toLowerCase();
        this.description = description;
    }

    /**
     * Получить название команды.
     *
     * @return Название команды.
     */
    public String getName() {
        return name;
    }

    /**
     * Получить описание команды.
     *
     * @return Описание команды.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Возвращает сообщение об ошибке при неправильном использовании команды.
     *
     * @return Сообщение об ошибке.
     */
    public String getUsingError() {
        return "Неправильное количество аргументов!\nИспользование: '" + getName() + getDescription() + "'";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return Objects.equals(name, command.name) && Objects.equals(description, command.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    @Override
    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
