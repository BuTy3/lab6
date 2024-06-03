package ru.itmo.common.exception;

/**
 * Исключение DuplicateException возникает при попытке добавления дубликата объекта.
 */
public class DuplicateException extends Exception {
    /**
     * Объект-дубликат.
     */
    private Object duplicateObject;

    /**
     * Конструктор с объектом-дубликатом.
     *
     * @param obj Объект-дубликат.
     */
    public DuplicateException(Object obj) {
        this.duplicateObject = obj;
    }

    /**
     * Пустой конструктор.
     */
    public DuplicateException() {}

    /**
     * Получить объект-дубликат.
     *
     * @return Объект-дубликат.
     */
    public Object getDuplicateObject() {
        return duplicateObject;
    }
}

