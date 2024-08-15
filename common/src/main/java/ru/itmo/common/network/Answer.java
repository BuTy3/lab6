package ru.itmo.common.network;

import lombok.Getter;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс Answer представляет ответ на запрос сетевого взаимодействия.
 * Расширяет абстрактный класс Networkable для работы с сетевыми данными.
 */
public class Answer extends Networkable implements Serializable {
    private static final int MAX_LENGTH = 394; // Максимальная длина одного сообщения в байтах
    @Getter
    private int responseCount;
    @Getter
    private int responseNumber;

    /**
     * Конструктор класса Answer с указанием всех полей.
     *
     * @param success Успешность операции.
     * @param message Сообщение о результате операции.
     * @param data    Данные, связанные с операцией.
     */
    public Answer(boolean success, String message, Object data) {
        super(success, message, data);
    }

    /**
     * Конструктор класса Answer с указанием успеха и сообщения.
     *
     * @param success Успешность операции.
     * @param message Сообщение о результате операции.
     */
    public Answer(boolean success, String message) {
        super(success, message, null);
    }

    private Answer(boolean success, String message, Object data, int responseCount, int responseNumber) {
        super(success, message, data);
        this.responseCount = responseCount;
        this.responseNumber = responseNumber;
    }

    /**
     * Переопределение метода toString для возвращения строкового представления объекта Answer.
     *
     * @return Строковое представление объекта Answer.
     */
    @Override
    public String toString() {
        return ((message != null) ? message : "") + (data != null ? ((message != null) ? '\n' + data.toString() : data.toString()) : "");
    }
}
