package ru.itmo.commands;

import ru.itmo.entities.StudyGroup;
import ru.itmo.managers.CollectionManager;
import ru.itmo.network.Answer;
import ru.itmo.network.Request;

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
            studyGroupCollectionManager.clearCollection();
            return new Answer(true, "Коллекция очищена!");
        } catch (Exception e){
            return new Answer(false, e.toString());
        }
    }
    /**
     * Выполняет команду
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
