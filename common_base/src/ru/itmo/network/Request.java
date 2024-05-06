package ru.itmo.network;

import ru.itmo.commands.CommandName;
import java.util.Objects;

/**
 * Класс Request представляет запрос для выполнения команды.
 * Расширяет класс Networkable для работы с сетевыми данными.
 */
public class Request extends Networkable{

    /**
     * Конструктор для создания запроса с указанием успешности, имени команды и данными.
     *
     * @param success Успешность выполнения запроса.
     * @param name    Имя команды.
     * @param data    Данные, связанные с запросом.
     */
    public Request(boolean success, String name, Object data){
        super(success, name, data);
    }

    /**
     * Конструктор для создания запроса с указанием успешности, имени команды и данными.
     *
     * @param success Успешность выполнения запроса.
     * @param name    Название команды (перечисление CommandName).
     * @param data    Данные, связанные с запросом.
     */
    public Request(boolean success, CommandName name, Object data) {
        this(success, name.toString().toLowerCase(), data);
    }

    /**
     * Конструктор для создания запроса с указанием имени команды и данными.
     *
     * @param name Название команды (перечисление CommandName).
     * @param data Данные, связанные с запросом.
     */
    public Request(CommandName name, Object data) {
        this(true, name, data);
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
     * Конструктор для создания запроса с указанием имени команды.
     *
     * @param name Название команды.
     */
    public Request(String name) {
        this(false, name, null);
    }

    /**
     * Конструктор для создания пустого запроса.
     */
    public Request(){
        this(false, "",null);
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

