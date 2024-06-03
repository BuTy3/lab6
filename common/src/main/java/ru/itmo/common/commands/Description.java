package ru.itmo.common.commands;

/**
 * Интерфейс Description предоставляет методы для получения имени и описания объекта.
 * Объекты, реализующие этот интерфейс, могут предоставлять информацию о себе.
 */
public interface Description {
    /**
     * Получить имя объекта.
     *
     * @return Имя объекта.
     */
    String getName();

    /**
     * Получить описание объекта.
     *
     * @return Описание объекта.
     */
    String getDescription();
}

