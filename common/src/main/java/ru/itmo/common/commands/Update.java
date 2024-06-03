package ru.itmo.common.commands;

import ru.itmo.common.entities.forms.Form;
import ru.itmo.common.entities.StudyGroup;
import ru.itmo.common.exception.EmptyValueException;
import ru.itmo.common.exception.NotFoundException;
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

            if(groupCollectionManager.update(id, new_group))
                return new Answer(true, "Группа успешно обновлен.");
            else{
                return new Answer(false, "Группа не обновлена, неизвестная ошибка");
            }
        } catch (EmptyValueException exception) {
            return new Answer(false, "Коллекция пуста!");
        } catch (NotFoundException exception) {
            return new Answer(false, "Группы с таким ID в коллекции нет!");
        }
    }
}
