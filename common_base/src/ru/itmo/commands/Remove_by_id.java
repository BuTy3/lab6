package ru.itmo.commands;

import ru.itmo.entities.StudyGroup;
import ru.itmo.exception.EmptyValueException;
import ru.itmo.exception.NotFoundException;
import ru.itmo.managers.CollectionManager;
import ru.itmo.network.Answer;
import ru.itmo.network.Request;
import ru.itmo.utility.Console;
import ru.itmo.exception.InvalidNumberOfElementsException;

import java.util.HashSet;
import java.util.Set;

public class Remove_by_id extends Command {
    private Console console;
    private CollectionManager<StudyGroup> groupCollectionManager;

    public Remove_by_id() {
        super(CommandName.REMOVE_BY_ID, "<ID> удалить ticket из коллекции по ID");
    }

    /**
     * Конструктор для создания экземпляра команды Remove.
     *
     * @param groupCollectionManager менеджер коллекции билетов
     */
    public Remove_by_id(CollectionManager<StudyGroup> groupCollectionManager) {
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
            if (!groupCollectionManager.remove(id)) throw new NotFoundException();

            return new Answer(true, "Группа успешно удалена.");

        } catch (EmptyValueException exception) {
            return new Answer(false, "Коллекция пуста!");
        } catch (NotFoundException exception) {
            return new Answer(false, "Группы с таким ID в коллекции нет!");
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
            return new Request(false, getName(), "ID должен быть представлен числом!");
        }
    }
    /**
     * Удаляет все учебные группы из коллекции, у которых количество студентов равно заданному значению.
     *
     * @param count Количество студентов, по которому производится фильтрация учебных групп для удаления.
     */
    private void removeAllBy(int count) {
        Set<StudyGroup> toDel = new HashSet<>();
        for (StudyGroup studyGroup : groupCollectionManager.getCollection()) {
            if (studyGroup.getStudentsCount() == count) {
                toDel.add(studyGroup);
            }
        }
        groupCollectionManager.getCollection().removeAll(toDel);
    }
}