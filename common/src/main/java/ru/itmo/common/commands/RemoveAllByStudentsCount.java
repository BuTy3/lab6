package ru.itmo.common.commands;

import ru.itmo.common.entities.StudyGroup;
import ru.itmo.common.exception.EmptyValueException;
import ru.itmo.common.exception.InvalidNumberOfElementsException;
import ru.itmo.common.io.Console;
import ru.itmo.common.managers.CollectionManager;
import ru.itmo.common.network.Answer;
import ru.itmo.common.network.Request;

import java.util.HashSet;
import java.util.Set;

public class RemoveAllByStudentsCount extends Command {
    private Console console;
    private CollectionManager<StudyGroup> studyGroupCollectionManager;

    public RemoveAllByStudentsCount() {
        super(CommandName.REMOVE_ALL_BY_STUDENTS_COUNT, "удалить элемент из коллекции по количеству студентов");
    }

    /**
     * Конструктор для создания экземпляра команды Remove.
     *
     * @param studyGroupCollectionManager менеджер коллекции
     */
    public RemoveAllByStudentsCount(CollectionManager<StudyGroup> studyGroupCollectionManager) {
        this();
        this.studyGroupCollectionManager = studyGroupCollectionManager;
    }

    @Override
    public Answer execute(Request request) {
        try {
            if (studyGroupCollectionManager.collectionSize() == 0) throw new EmptyValueException();

            // Извлечение количества студентов из запроса
            var count = ((Integer) request.getData());

            // Удаление всех групп с указанным количеством студентов
            removeAllBy(count);

            // Возвращаем ответ об успешном удалении
            return new Answer(true, "Группы с количеством студентов " + count + " успешно удалены.");

        } catch (EmptyValueException exception) {
            return new Answer(false, "Коллекция пуста!");
        }
    }

    /**
     * Выполняет команду.
     *
     * @param arguments аргументы команды.
     * @return Успешность выполнения команды.
     */
    @Override
    public Request execute(String[] arguments) {
        try {
            if (arguments.length < 2 || arguments[1].isEmpty()) throw new InvalidNumberOfElementsException();

            // Парсинг количества студентов из аргументов
            int count = Integer.parseInt(arguments[1]);
            return new Request(getName(), count);
        } catch (InvalidNumberOfElementsException exception) {
            return new Request(false, getName(), getUsingError());
        } catch (NumberFormatException exception) {
            return new Request(false, getName(), "Количество студентов должно быть числом!");
        }
    }

    /**
     * Удаляет все учебные группы из коллекции, у которых количество студентов равно заданному значению.
     *
     * @param count Количество студентов, по которому производится фильтрация учебных групп для удаления.
     */
    private void removeAllBy(int count) {
        Set<StudyGroup> toDel = new HashSet<>();
        for (StudyGroup studyGroup : studyGroupCollectionManager.getCollection()) {
            if (studyGroup.getStudentsCount() == count) {
                toDel.add(studyGroup);
            }
        }
        studyGroupCollectionManager.getCollection().removeAll(toDel);
    }

//    /**
//     * Выполняет команду
//     *
//     * @param request аргументы команды
//     * @return Успешность выполнения команды.
//     */
//    @Override
//    public Answer execute(Request request) {
//        try {
//
//            if (studyGroupCollectionManager.collectionSize() == 0) throw new EmptyValueException();
//
//            var count = ((Integer) request.getData());
//            removeAllBy(count);
//
//            return new Answer(true, "Группа успешно удалена.");
//
//        } catch (EmptyValueException exception) {
//            return new Answer(false, "Коллекция пуста!");
//        }
//    }
//
//    /**
//     * Выполняет команду
//     *
//     * @param arguments аргументы команды
//     * @return Успешность выполнения команды.
//     */
//    @Override
//    public Request execute(String[] arguments) {
//        try {
//            if (arguments.length < 2 || arguments[1].isEmpty()) throw new InvalidNumberOfElementsException();
//
//            int id = Integer.parseInt(arguments[1]);
//            return new Request(getName(), id);
//        } catch (InvalidNumberOfElementsException exception) {
//            return new Request(false, getName(), getUsingError());
//        } catch (NumberFormatException exception) {
//            return new Request(false, getName(), "ID должен быть представлен числом!");
//        }
//    }
//    /**
//     * Удаляет все учебные группы из коллекции, у которых количество студентов равно заданному значению.
//     *
//     * @param count Количество студентов, по которому производится фильтрация учебных групп для удаления.
//     */
//    private void removeAllBy(int count) {
//        Set<StudyGroup> toDel = new HashSet<>();
//        for (StudyGroup studyGroup : studyGroupCollectionManager.getCollection()) {
//            if (studyGroup.getStudentsCount() == count) {
//                toDel.add(studyGroup);
//            }
//        }
//        studyGroupCollectionManager.getCollection().removeAll(toDel);
//    }
}