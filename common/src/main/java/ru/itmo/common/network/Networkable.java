package ru.itmo.common.network;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Абстрактный класс Networkable, представляющий объект, который может быть передан по сети.
 * Реализует интерфейс Serializable для сериализации объекта.
 */
@Getter
@Setter
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
     * Login credentials associated with the request, if applicable.
     */
    protected String login;

    /**
     * Password credentials associated with the request, if applicable.
     */
    protected String password;

    /**
     * User ID associated with the request, if applicable.
     */
    protected Integer userId;

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
}

