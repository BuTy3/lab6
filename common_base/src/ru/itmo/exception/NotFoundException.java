package ru.itmo.exception;

/**
 * Что-то не найдено
 */
public class NotFoundException extends Exception {
    private String message;
    public NotFoundException(){
        this.message = "";
    }
    public NotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
