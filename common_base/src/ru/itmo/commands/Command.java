package ru.itmo.commands;

import java.util.Objects;

/**
 * Абстрактный класс Command представляет собой команду, которую можно выполнить.
 * Реализует интерфейсы Describable и Executable для получения описания команды и ее выполнения.
 */
public abstract class Command implements Description, Execute {
    private final String name;
    private final String description;

    /**
     * Конструктор для создания команды с указанием имени и описания.
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
     * Получить сообщение об ошибке использования команды.
     *
     * @return Сообщение об ошибке использования команды.
     */
    public String getUsingError() {
        return "Использование: '" + getName() + getDescription() + "'";
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
                ", description='" + description + '\'' + '}';
    }
}

