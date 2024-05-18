package ru.itmo.utility;

/**
 * Интерфейс Validatable представляет метод для проверки валидности объекта.
 */
public interface Validatable {
    /**
     * Проверяет, является ли объект валидным.
     *
     * @return true, если объект валиден, иначе false.
     */
    boolean validate();
}

