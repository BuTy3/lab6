package ru.itmo.commands;

import ru.itmo.entities.Form;
import ru.itmo.entities.StudyGroup;
import ru.itmo.exception.EmptyValueException;
import ru.itmo.exception.NotFoundException;
import ru.itmo.managers.CollectionManager;
import ru.itmo.network.Answer;
import ru.itmo.network.Request;
import ru.itmo.utility.Console;

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
            var group = groupCollectionManager.byId(id);
            if (group == null) throw new NotFoundException();

            group.update(new_group);

            return new Answer(true, "Группа успешно обновлен.");

        } catch (EmptyValueException exception) {
            return new Answer(false, "Коллекция пуста!");
        } catch (NotFoundException exception) {
            return new Answer(false, "Группы с таким ID в коллекции нет!");
        }
    }
}
