package ru.itmo.commands;

import ru.itmo.entities.Form;
import ru.itmo.entities.StudyGroup;
import ru.itmo.exception.InvalidFormException;
import ru.itmo.exception.InvalidNumberOfElementsException;
import ru.itmo.exception.InvalidScriptInputException;
import ru.itmo.managers.CollectionManager;
import ru.itmo.network.Answer;
import ru.itmo.network.Request;
import ru.itmo.utility.Console;

import java.util.HashSet;
import java.util.Set;

public class RemoveGreater extends Command {
    private Console console;
    private CollectionManager<StudyGroup> studyGroupCollectionManager;
    private Form<StudyGroup> StForm;

    public RemoveGreater() {
        super(CommandName.REMOVE_GREATER, "удалить из коллекции все элементы больше заданного");
    }

    public RemoveGreater(CollectionManager<StudyGroup> studyGroupCollectionManager) {
        this();
        this.studyGroupCollectionManager = studyGroupCollectionManager;
    }

    public RemoveGreater(Console console, Form<StudyGroup> StForm) {
        this();
        this.console = console;
        this.StForm = StForm;
    }

    @Override
    public Answer execute(Request answer) {
        try {
            var group = ((StudyGroup) answer.getData());
            for (StudyGroup c : studyGroupCollectionManager.getCollection()) {
                if (c.compareTo(group) > 0)
                    studyGroupCollectionManager.remove(c);
            }
        } catch (Exception e) {
            return new Answer(false, e.toString(), -1);
        }
        return new Answer(true, "Команда выполнена успешно, удалено:", -1);
    }

        /**
     * Выполняет команду.
     *
     * @param arguments аргументы команды (ожидается отсутствие аргументов)
     * @return Успешность выполнения команды
     */
    @Override
    public Request execute(String[] arguments) {
        try {
            if (!arguments[1].isEmpty()) throw new InvalidNumberOfElementsException();
            console.println("* Создание группы:");

            var newGroup = StForm.build();
            return new Request(getName(), newGroup);

        } catch (InvalidNumberOfElementsException exception) {
            return new Request(false, getName(), getUsingError());
        } catch (InvalidFormException exception) {
            return new Request(false, getName(), "Поля группы не валидны! Группа не создана!");
        } catch (InvalidScriptInputException ignored) {
            return new Request(false, getName(), "Ошибка чтения из скрипта");
        }
    }
    private void removeAllBy(int count) {
        Set<StudyGroup> toDel = new HashSet<>();
        for (StudyGroup studyGroup : studyGroupCollectionManager.getCollection()) {
            if (studyGroup.getStudentsCount() == count) {
                toDel.add(studyGroup);
            }
        }
        studyGroupCollectionManager.getCollection().removeAll(toDel);
    }
}