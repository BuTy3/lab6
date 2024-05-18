package ru.itmo.exception;

/**
 * Ошибка при ответе
 */
public class RequestException extends Exception {
    private String message;
    public RequestException(){
        this.message = "";
    }
    public RequestException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}