package ru.itmo.common.network;

import java.io.Serializable;
import java.util.Objects;

/**
 * Класс Request представляет запрос для выполнения команды.
 * Расширяет класс Networkable для работы с сетевыми данными.
 */
public class Request extends Networkable implements Serializable {

    /**
     * Конструктор для создания запроса с указанием успешности, имени команды и данными.
     *
     * @param success Успешность выполнения запроса.
     * @param name    Имя команды.
     * @param data    Данные, связанные с запросом.
     */
    public Request(boolean success, String name, Object data) {
        super(success, name, data);
    }

    /**
     * Конструктор для создания запроса с указанием имени команды и данными.
     *
     * @param name Название команды.
     * @param data Данные, связанные с запросом.
     */
    public Request(String name, Object data) {
        this(true, name, data);
    }


    /**
     * Получить имя команды.
     *
     * @return Имя команды.
     */
    public String getCommand() {
        return getMessage();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return Objects.equals(getCommand(), request.getCommand());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommand());
    }

    @Override
    public String toString() {
        return "Request{" +
                "command='" + getCommand() + '\'' +
                '}';
    }
}

