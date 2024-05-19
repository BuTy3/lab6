package ru.itmo.commands;

import ru.itmo.network.Request;

public class History extends Command {

    /**
     * Конструктор для создания экземпляра команды History.
     *
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
        return new Request(getName(), null);
    }
}
