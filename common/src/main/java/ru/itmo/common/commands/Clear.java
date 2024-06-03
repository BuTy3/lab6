package ru.itmo.common.commands;

import ru.itmo.common.entities.StudyGroup;
import ru.itmo.common.managers.CollectionManager;
import ru.itmo.common.network.Answer;
import ru.itmo.common.network.Request;

public class Clear extends Command {
    private CollectionManager<StudyGroup> studyGroupCollectionManager;

    public Clear() {
        super(CommandName.CLEAR, "очистить коллекцию");
    }

    public Clear(CollectionManager<StudyGroup> studyGroupCollectionManager) {
        this();
        this.studyGroupCollectionManager = studyGroupCollectionManager;
    }

    /**
     * Выполняет команду
     *
     * @return Успешность выполнения команды.
     */
    @Override
    public Answer execute(Request arguments) {
        try {
            studyGroupCollectionManager.clear();
            return new Answer(true, "Коллекция очищена!");
        } catch (Exception e) {
            return new Answer(false, e.toString());
        }
    }

    /**
     * Выполняет команду
     *
     * @return Успешность выполнения команды.
     */
    @Override
    public Request execute(String[] arguments) {
        if (!arguments[1].isEmpty()) {
            return new Request(false, getName(), getUsingError());
        }
        return new Request(getName(), null);
    }
}
