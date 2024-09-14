package ru.itmo.common.commands;

import ru.itmo.common.managers.CommandManager;
import ru.itmo.common.network.Answer;
import ru.itmo.common.network.Request;

public class History extends Command {

    /**
     * Конструктор для создания экземпляра команды History.
     */
    public History() {
        super(CommandName.HISTORY, "вывести список использованных команд");
    }


    /**
     * Выполняет команду.
     *
     * @param arguments аргументы команды (в данном случае ожидается отсутствие аргументов)
     * @return Успешность выполнения команды
     */
    @Override
    public Request execute(String[] arguments) {
        if (arguments.length > 1 && !arguments[1].isEmpty()) {
            return new Request(false, getName(), getUsingError());
        }
        return new Request(getName(), CommandManager.get_commandHistory());
    }

    /**
     * Выполняет команду.
     *
     * @param request запрос, содержащий аргументы команды (в данном случае ожидается отсутствие аргументов)
     * @return ответ на выполнение команды
     */
    @Override
    public Answer execute(Request request) {
        return new Answer(true, getName(), CommandManager.get_commandHistory().toString());
    }
}

