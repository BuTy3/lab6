package ru.itmo.common.commands;

import ru.itmo.common.entities.forms.Form;
import ru.itmo.common.entities.StudyGroup;
import ru.itmo.common.exception.InvalidFormException;
import ru.itmo.common.exception.InvalidNumberOfElementsException;
import ru.itmo.common.exception.InvalidScriptInputException;
import ru.itmo.common.managers.CollectionManager;
import ru.itmo.common.network.Answer;
import ru.itmo.common.network.Request;
import ru.itmo.common.io.Console;

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
            if (!group.validate()) {
                return new Answer(false, "Группа не добавлена, поля не валидны!");
            }
            if(!studyGroupCollectionManager.add(group))
                return new Answer(false, "Группа уже существует", -1);
            return new Answer(true, "Группа добавлена", group.getId());
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
            console.println("* Создание новой группы:");

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