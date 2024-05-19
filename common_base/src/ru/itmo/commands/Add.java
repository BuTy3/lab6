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

public class Add extends Command {
    private Console console;
    private CollectionManager<StudyGroup> studyGroupCollectionManager;
    private Form<StudyGroup> StForm;

    public Add() {
        super(CommandName.ADD, "{element} добавить новый объект Группа в коллекцию");
    }

    /**
     * Конструктор для создания экземпляра команды Add.
     *
     * @param studyGroupCollectionManager менеджер коллекции
     */
    public Add(CollectionManager<StudyGroup> studyGroupCollectionManager) {
        this();
        this.studyGroupCollectionManager = studyGroupCollectionManager;
    }

    public Add(Console console, Form<StudyGroup> StForm) {
        this();
        this.console = console;
        this.StForm = StForm;
    }

    @Override
    public Answer execute(Request answer) {
        try {
            var group = ((StudyGroup)answer.getData());
            group.setId(StudyGroup.getNextId());
            if (!group.validate()) {
                return new Answer(false, "Группа не добавлен, поля не валидны!");
            }
            group.setId(studyGroupCollectionManager.getFreeId());
            if(!studyGroupCollectionManager.add(group)) return new Answer(false, "Группа уже существует", -1);
            return new Answer(true, null, studyGroupCollectionManager.getFreeId());
        } catch (Exception e) {
            return new Answer(false, e.toString(), -1);
        }
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
            console.println("* Создание нового продукта:");

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
}