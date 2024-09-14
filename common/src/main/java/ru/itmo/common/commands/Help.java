package ru.itmo.common.commands;

import ru.itmo.common.managers.CommandManager;
import ru.itmo.common.network.Request;

public class Help extends Command {

    /**
     * Конструктор для создания экземпляра команды Help.
     */
    public Help() {
        super(CommandName.HELP, "вывести справку по доступным командам");
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

        return new Request(getName(), CommandManager.help());
    }
}