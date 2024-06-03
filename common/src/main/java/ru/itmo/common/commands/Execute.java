package ru.itmo.common.commands;

import ru.itmo.common.network.Answer;
import ru.itmo.common.network.Request;

/**
 * Интерфейс для всех команд, которые могут быть выполнены.
 * Команды, реализующие этот интерфейс, могут выполняться с заданными аргументами.
 */
public interface Execute {
    /**
     * Выполнить команду с заданными аргументами.
     *
     * @param arguments Аргументы команды.
     * @return Ответ на выполнение команды.
     */
    default Answer execute(Request arguments){return null;};

    /**
     * Выполнить команду с заданными аргументами.
     *
     * @param arguments Аргументы команды в виде массива строк.
     * @return Ответ на выполнение команды.
     */
    default Request execute(String[] arguments){return null;};
}
