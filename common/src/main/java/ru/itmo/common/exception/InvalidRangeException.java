package ru.itmo.common.exception;

/**
 *Что-то введено вне диапазона допустимых значений.
 */
public class InvalidRangeException extends Exception {
    private String message;

    /**
     * Конструктор по умолчанию.
     */
    public InvalidRangeException() {}

    /**
     * Конструктор с сообщением об ошибке.
     *
     * @param s сообщение об ошибке
     */
    public InvalidRangeException(String s) {
        this.message = s;
    }

    /**
     * Возвращает сообщение об ошибке.
     *
     * @return сообщение об ошибке
     */
    public String getMessage() {
        return message;
    }
}