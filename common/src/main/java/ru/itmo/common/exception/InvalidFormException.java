package ru.itmo.common.exception;

/**
 * Выбрасывается, если в форме создан невалидный объект.
 */
public class InvalidFormException extends Exception {
    public InvalidFormException() {
    }

    public InvalidFormException(String message) {
        super(message);
    }
}
