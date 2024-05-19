package ru.itmo.commands;

import ru.itmo.entities.StudyGroup;
import ru.itmo.managers.CollectionManager;
import ru.itmo.network.Answer;
import ru.itmo.network.Request;

public class Show extends Command {
    private CollectionManager<StudyGroup> groupCollectionManager;

    public Show() {
        super(CommandName.SHOW, "вывести все элементы коллекции StudyGroup");
    }
    /**
     * Конструктор для создания экземпляра команды Show.
     *
     * @param groupCollectionManager менеджер коллекции
     */
    public Show(CollectionManager<StudyGroup> groupCollectionManager) {
        this();
        this.groupCollectionManager = groupCollectionManager;
    }

    /**
     * Выполняет команду
     *
     * @param arguments аргументы команды
     * @return Успешность выполнения команды.
     */
    @Override
    public Answer execute(Request arguments) {

        String message = groupCollectionManager.toString();
        return new Answer(true, null, message);
    }
    /**
     * Выполняет команду
     *
     * @param arguments аргументы команды
     * @return Успешность выполнения команды.
     */
    @Override
    public Request execute(String[] arguments) {
        if (!arguments[1].isEmpty()) {
            return new Request(false, getName(), getUsingError());
        }
        return new Request(getName(), null);
    }
}
