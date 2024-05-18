package ru.itmo.commands;

import ru.itmo.entities.StudyGroup;
import ru.itmo.managers.CollectionManager;
import ru.itmo.network.Answer;
import ru.itmo.network.Request;

import java.time.LocalDateTime;

public class Info extends Command {
    private CollectionManager<StudyGroup> studyGroupCollectionManager;

    public Info() {
        super(CommandName.INFO, "вывести информацию о коллекции");
    }

    /**
     * Конструктор для создания экземпляра команды Info.
     *
     * @param studyGroupCollectionManager менеджер коллекции билетов
     */
    public Info(CollectionManager<StudyGroup> studyGroupCollectionManager) {
        this();
        this.studyGroupCollectionManager = studyGroupCollectionManager;
    }

    @Override
    public Answer execute (Answer arguments) {
        String message;

        LocalDateTime lastSaveTime = studyGroupCollectionManager.getLastSaveTime();
        String lastSaveTimeString = (lastSaveTime == null) ? "в данной сессии сохранения еще не происходило" :
                lastSaveTime.toLocalDate().toString() + " " + lastSaveTime.toLocalTime().toString();

        message = "Сведения о коллекции:\n" +
        " Тип: " + studyGroupCollectionManager.collectionType() +'\n' +
        " Количество элементов: " + studyGroupCollectionManager.collectionSize() + '\n' +
        " Дата последнего сохранения:" + lastSaveTimeString + '\n' +
        " Дата инициализации:" + studyGroupCollectionManager.getInitializationDate();
        return new Answer(true, null, message);
    }

    @Override
    public Request execute(String[] arguments) {
        if (arguments.length > 1 && !arguments[1].isEmpty()) {
            return new Request(false, getName(), getUsingError());
        }

        return new Request(getName(), null);
    }
}