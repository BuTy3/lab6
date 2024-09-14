package ru.itmo.common.commands;

import ru.itmo.common.io.Console;
import ru.itmo.common.network.Answer;
import ru.itmo.common.network.Request;

public class Exit extends Command {
    private Console console;

    /**
     * Конструктор для создания экземпляра команды Exit.
     */
    public Exit() {
        super(CommandName.EXIT, "завершить работу приложения");
    }

    /**
     * Конструктор для создания экземпляра команды Exit.
     *
     * @param console объект для взаимодействия с консолью
     */
    public Exit(Console console) {
        this();
        this.console = console;
    }

    /**
     * Выполняет команду.
     *
     * @param arguments аргументы команды (ожидается отсутствие аргументов)
     * @return Успешность выполнения команды
     */
    @Override
    public Answer execute(Request arguments) {
        try {
            return new Answer(true, null);
        } catch (Exception e) {
            return new Answer(false, e.toString());
        }
    }

    /**
     * Выполняет команду
     *
     * @return Успешность выполнения команды.
     */
    @Override
    public Request execute(String[] arguments) {
        if (!arguments[1].isEmpty()) {
            return new Request(false, getName(), getUsingError());
        }

        return new Request(getName(), "Завершение работы :(");
    }
}