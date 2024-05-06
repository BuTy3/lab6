package ru.itmo.commands;

import ru.itmo.network.Answer;
import ru.itmo.network.Request;

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
    default Answer execute(Answer arguments){return null;};

    /**
     * Выполнить команду с заданными аргументами.
     *
     * @param arguments Аргументы команды в виде массива строк.
     * @return Ответ на выполнение команды.
     */
    default Request execute(String[] arguments){return null;};
}
