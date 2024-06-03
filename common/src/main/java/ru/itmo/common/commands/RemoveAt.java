package ru.itmo.common.commands;

import ru.itmo.common.entities.StudyGroup;
import ru.itmo.common.exception.EmptyValueException;
import ru.itmo.common.exception.InvalidNumberOfElementsException;
import ru.itmo.common.exception.NotFoundException;
import ru.itmo.common.managers.CollectionManager;
import ru.itmo.common.network.Answer;
import ru.itmo.common.network.Request;
import ru.itmo.common.io.Console;

public class RemoveAt extends Command {
    private Console console;
    private CollectionManager<StudyGroup> groupCollectionManager;

    public RemoveAt() {
        super(CommandName.REMOVE_BY_AT, "удалить элемент из коллекции по индексу");
    }

    /**
     * Конструктор для создания экземпляра команды Remove.
     *
     * @param groupCollectionManager менеджер коллекции
     */
    public RemoveAt(CollectionManager<StudyGroup> groupCollectionManager) {
        this();
        this.groupCollectionManager = groupCollectionManager;
    }

    /**
     * Выполняет команду
     *
     * @param request аргументы команды
     * @return Успешность выполнения команды.
     */
    @Override
    public Answer execute(Request request) {
        try {

            if (groupCollectionManager.collectionSize() == 0) throw new EmptyValueException();

            var id = ((Integer) request.getData());
            if (!groupCollectionManager.getCollection().remove(id)) throw new NotFoundException();

            return new Answer(true, "Группа успешно удалена.");

        } catch (EmptyValueException exception) {
            return new Answer(false, "Коллекция пуста!");
        } catch (NotFoundException exception) {
            return new Answer(false, "Группы с таким индексом в коллекции нет!");
        }
    }

    /**
     * Выполняет команду
     *
     * @param arguments аргументы команды
     * @return Успешность выполнения команды.
     */
    @Override
    public Request execute(String[] arguments) {
        try {
            if (arguments.length < 2 || arguments[1].isEmpty()) throw new InvalidNumberOfElementsException();

            int id = Integer.parseInt(arguments[1]);
            return new Request(getName(), id);
        } catch (InvalidNumberOfElementsException exception) {
            return new Request(false, getName(), getUsingError());
        } catch (NumberFormatException exception) {
            return new Request(false, getName(), "Индекс должен быть представлен числом!");
        }
    }
}