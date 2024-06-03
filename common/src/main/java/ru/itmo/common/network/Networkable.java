package ru.itmo.common.network;

import java.io.Serializable;

/**
 * Абстрактный класс Networkable, представляющий объект, который может быть передан по сети.
 * Реализует интерфейс Serializable для сериализации объекта.
 */
public abstract class Networkable implements Serializable {
    /**
     * Поле, указывающее на успешность операции.
     */
    boolean success;

    /**
     * Поле для хранения данных.
     */
    Object data;

    /**
     * Поле для хранения сообщения о результате операции.
     */
    String message;

    /**
     * Конструктор класса Networkable.
     *
     * @param success Успешность операции.
     * @param message Сообщение о результате операции.
     * @param data    Данные, связанные с операцией.
     */
    public Networkable(final boolean success, String message, final Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public Networkable() {

    }

    /**
     * Метод для получения данных.
     *
     * @return Данные, связанные с операцией.
     */
    public Object getData() {
        return data;
    }

    /**
     * Метод для проверки успешности операции.
     *
     * @return true, если операция выполнена успешно, иначе false.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Метод для получения сообщения о результате операции.
     *
     * @return Сообщение о результате операции.
     */
    public String getMessage() {
        return message;
    }
}

