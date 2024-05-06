package ru.itmo.network;

/**
 * Класс Answer представляет ответ на запрос сетевого взаимодействия.
 * Расширяет абстрактный класс Networkable для работы с сетевыми данными.
 */
public class Answer extends Networkable {

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

    /**
     * Конструктор класса Answer с указанием только успеха.
     *
     * @param success Успешность операции.
     */
    public Answer(boolean success) {
        super(success, null, null);
    }

    /**
     * Переопределение метода toString для возвращения строкового представления объекта Answer.
     *
     * @return Строковое представление объекта Answer.
     */
    @Override
    public String toString() {
        // Формируем строку, содержащую сообщение и данные (если они есть)
        return ((message != null) ? message : "") + (data != null ? ((message != null) ? '\n' + data.toString() : data.toString()) : "");
    }
}

