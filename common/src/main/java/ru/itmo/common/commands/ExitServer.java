package ru.itmo.common.commands;

import ru.itmo.common.entities.StudyGroup;
import ru.itmo.common.managers.CollectionManager;
import ru.itmo.common.network.Answer;
import ru.itmo.common.network.Request;

public class ExitServer extends Command {
    private CollectionManager groupCollectionManager;
    public ExitServer() {
        super(CommandName.EXIT_SERVER, "завершить программу (с сохранением в файл)");
    }
    /**
     * Конструктор для создания экземпляра команды Exit.
     *
     */
    public ExitServer(CollectionManager<StudyGroup> groupCollectionManager) {
        this();
        this.groupCollectionManager = groupCollectionManager;
    }

    /**
     * Выполняет команду.
     *
     * @param arguments аргументы команды (ожидается отсутствие аргументов)
     * @return Успешность выполнения команды
     */
    @Override
    public Answer execute(Request arguments) {
        try {
            return new Answer(true, null);
        } catch (Exception e){
            return new Answer(false, e.toString());
        }
    }
}
