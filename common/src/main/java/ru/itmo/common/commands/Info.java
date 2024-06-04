package ru.itmo.common.commands;

import ru.itmo.common.entities.StudyGroup;
import ru.itmo.common.managers.CollectionManager;
import ru.itmo.common.network.Answer;
import ru.itmo.common.network.Request;

public class Info extends Command {
    private CollectionManager<StudyGroup> studyGroupCollectionManager;

    public Info() {
        super(CommandName.INFO, "вывести информацию о коллекции");
    }

    /**
     * Конструктор для создания экземпляра команды Info.
     *
     * @param studyGroupCollectionManager менеджер коллекции
     */
    public Info(CollectionManager<StudyGroup> studyGroupCollectionManager) {
        this();
        this.studyGroupCollectionManager = studyGroupCollectionManager;
    }

    @Override
    public Answer execute(Request arguments) {
        String message;
        try {
            message = studyGroupCollectionManager.description();
            return new Answer(true, null, message);
        } catch (Exception e) {
            return new Answer(false, null, e.getMessage());
        }
    }

    @Override
    public Request execute(String[] arguments) {
        if (arguments.length > 1 && !arguments[1].isEmpty()) {
            return new Request(false, getName(), getUsingError());
        }

        return new Request(getName(), null);
    }
}