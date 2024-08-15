package ru.itmo.common.commands;

import ru.itmo.common.entities.forms.Form;
import ru.itmo.common.entities.StudyGroup;
import ru.itmo.common.entities.forms.StudyGroupForm;
import ru.itmo.common.exception.*;
import ru.itmo.common.managers.CollectionManager;
import ru.itmo.common.network.Answer;
import ru.itmo.common.network.Request;
import ru.itmo.common.io.Console;


public class Update extends Command {
    private CollectionManager<StudyGroup> groupCollectionManager;
    private Console console;
    private Form<StudyGroup> groupForm;

    public Update() {
        super(CommandName.UPDATE, "<ID> {element} обновить значение элемента коллекции по ID");
    }

    public Update(CollectionManager<StudyGroup> groupCollectionManager) {
        this();
        this.groupCollectionManager = groupCollectionManager;
    }

    public Update(Console console, Form<StudyGroup> groupForm) {
        this();
        this.console = console;
        this.groupForm = groupForm;
    }

    /**
     * Выполняет команду
     *
     * @return Успешность выполнения команды.
     */
    @Override
    public Answer execute(Request request) {
        try {
            if (groupCollectionManager.collectionSize() == 0) throw new EmptyValueException();

            var new_group = ((StudyGroup) request.getData());
            var id = new_group.getId();
            var group = groupCollectionManager.get(id);
            if (group == null) throw new NotFoundException();

            if (groupCollectionManager.update(id, new_group))
                return new Answer(true, "Группа успешно обновлен.");
            else {
                return new Answer(false, "Группа не обновлена, неизвестная ошибка");
            }
        } catch (EmptyValueException exception) {
            return new Answer(false, "Коллекция пуста!");
        } catch (NotFoundException exception) {
            return new Answer(false, "Группы с таким ID в коллекции нет!");
        } catch (Exception e) {
            return new Answer(false, "Неизвестная ошибка");

        }

    }

    @Override
    public Request execute(String[] arguments) {
        try {
            if (arguments.length <= 1 || arguments[1].isEmpty()) throw new InvalidNumberOfElementsException();

            var id = Integer.parseInt(arguments[1]);

            var newGroup = groupForm.build();
            newGroup.setId((long) id);
            return new Request(getName(), newGroup);
        } catch (InvalidNumberOfElementsException exception) {
            return new Request(false, getName(), getUsingError());
        } catch (NumberFormatException exception) {
            return new Request(false, getName(), "ID должен быть числом!");
        } catch (InvalidScriptInputException e) {
            return new Request(false, getName(), "Invalid input in the script!");
        } catch (InvalidFormException e) {
            return new Request(false, getName(), "Группа не валидна, добавление не выполнено!");
        }
    }
}
