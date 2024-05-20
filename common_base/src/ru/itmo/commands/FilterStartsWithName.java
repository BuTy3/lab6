package ru.itmo.commands;

import ru.itmo.entities.Form;
import ru.itmo.entities.StudyGroup;
import ru.itmo.exception.EmptyValueException;
import ru.itmo.exception.InvalidNumberOfElementsException;
import ru.itmo.exception.NotFoundException;
import ru.itmo.managers.CollectionManager;
import ru.itmo.network.Answer;
import ru.itmo.network.Request;
import ru.itmo.utility.Console;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FilterStartsWithName extends Command {
    private Console console;
    private CollectionManager<StudyGroup> studyGroupCollectionManager;


    public FilterStartsWithName() {
        super(CommandName.FILTER_STARTS_WITH_NAME, "Выводит элементы равные имени");
    }

    public FilterStartsWithName(CollectionManager<StudyGroup> studyGroupCollectionManager) {
        this();
        this.studyGroupCollectionManager = studyGroupCollectionManager;
    }

    public FilterStartsWithName(Console console) {
        this();
        this.console = console;
    }

    public Answer execute(Request request) {
        try {
            if (studyGroupCollectionManager.collectionSize() == 0) throw new EmptyValueException();

            var name = ((String) request.getData());
            List<String> matchingNames = new ArrayList<>();
            for (StudyGroup group : studyGroupCollectionManager.getCollection()) {
                if (Objects.equals(group.getName(), name)) {
                    matchingNames.add(group.getName());
                }
            }
            if (matchingNames.isEmpty()) {
                return new Answer(false, "Группы не найдены");
            }
            return new Answer(true, null, matchingNames);
        } catch (EmptyValueException e) {
            return new Answer(false, "Коллекция пуста!");
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
            if (arguments[1].isEmpty()) throw new InvalidNumberOfElementsException();
            if (studyGroupCollectionManager.collectionSize() == 0) throw new EmptyValueException();
            var name = arguments[1];
            return new Request(getName(), name);
        } catch (InvalidNumberOfElementsException exception) {
            return new Request(false, getName(), getUsingError());
        } catch (NumberFormatException | EmptyValueException exception) {
            return new Request(false, getName(), "Неверный формат имени");
        }
    }
}