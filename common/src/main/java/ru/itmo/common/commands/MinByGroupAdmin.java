package ru.itmo.common.commands;

import ru.itmo.common.entities.Person;
import ru.itmo.common.entities.StudyGroup;
import ru.itmo.common.exception.EmptyValueException;
import ru.itmo.common.exception.InvalidNumberOfElementsException;
import ru.itmo.common.managers.CollectionManager;
import ru.itmo.common.network.Answer;
import ru.itmo.common.network.Request;

public class MinByGroupAdmin extends Command {
    private CollectionManager<StudyGroup> groupCollectionManager;
    public MinByGroupAdmin(){
        super(CommandName.MIN_BY_GROUP_ADMIN, "вывести любой объект из коллекции, значение поля groupAdmin которого является минимальным");
    }

    public MinByGroupAdmin(CollectionManager<StudyGroup> groupCollectionManager) {
        this();
        this.groupCollectionManager = groupCollectionManager;
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
            StudyGroup group = minByGroupAdmin();
            return new Answer(true, null, group.toString());

        } catch (EmptyValueException exception) {
            return new Answer(false, "Коллекция пуста!", null);
        }
    }

    /**
     * Выполняет команду
     *
     * @return Успешность выполнения команды.
     */
    @Override
    public Request execute(String[] arguments) {
        try {
            if (arguments.length > 1 && !arguments[1].isEmpty()) throw new InvalidNumberOfElementsException();
            return new Request(getName(), null);
        } catch (InvalidNumberOfElementsException exception) {
            return new Request(false, getName(), getUsingError());
        }
    }

    private StudyGroup minByGroupAdmin() {
        Person minPerson = groupCollectionManager.getCollection().get(0).getGroupAdmin();
        long groupID = -1;
        for (StudyGroup c : groupCollectionManager.getCollection()) {
            if (minPerson == null) {
                if (c.getGroupAdmin() == null) {
                    continue;
                } else {
                    minPerson = c.getGroupAdmin();
                    continue;
                }
            }
            if (c.getGroupAdmin() == null) {
                continue;
            }
            if (c.getGroupAdmin().compareTo(minPerson) < 0) {
                minPerson = c.getGroupAdmin();
                groupID = c.getId();
            }
        }
        if (groupID == -1) return groupCollectionManager.getCollection().get(0);
        return groupCollectionManager.get(groupID);
    }
}