package ru.itmo.server.main;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.common.entities.StudyGroup;
import ru.itmo.common.entities.User;
import ru.itmo.common.io.Console;
import ru.itmo.common.io.StandartConsole;
import ru.itmo.common.managers.CommandManager;
import ru.itmo.server.dao.StudyGroupDAO;
import ru.itmo.server.dao.UserDAO;
import ru.itmo.server.database.DatabaseManager;
import ru.itmo.server.managers.StudyGroupCollectionManager;
import ru.itmo.server.network.Server;

import java.io.File;
import java.util.Scanner;

/**
 *
 */
public class Main {
    public static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final Console console = new StandartConsole();
    private static final int PORT = 4202;

    public static void main(String[] args) {
        String filePath = System.getenv("GROUPS_FILE_PATH");

        if (filePath == null || filePath.isEmpty()) {
            logger.error("Переменная среды 'GROUPS_FILE_PATH' не задана или пуста.");
            return; // Завершаем работу приложения, если путь не задан
        }
        if (!(new File(filePath)).exists()) {
            if (!new File("../" + filePath).exists()) {
                logger.error("Файл не существует");
                return;
            } else {
                filePath = "../" + filePath;
            }
        }

        StudyGroupCollectionManager groupCollection = new StudyGroupCollectionManager();
        DatabaseManager.createDatabaseIfNotExists();
        StudyGroupDAO studyGroupDAO = new StudyGroupDAO();
        UserDAO userDAO = new UserDAO();
        groupCollection.loadCollection(studyGroupDAO);
        startConsoleListener(groupCollection);

        CommandManager.initServerCommands(groupCollection, userDAO);
        Server server = new Server(PORT);
        server.start();
    }

    /**
     * Метод запускает слушатель консоли для ввода команд пользователем.
     *
     * @param collectionManager менеджер коллекции
     */
    private static void startConsoleListener(StudyGroupCollectionManager collectionManager) {
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String input = scanner.nextLine().trim();
                if ("exit".equalsIgnoreCase(input)) {
                    logger.info("Завершение работы программы...");
                    //collectionManager.saveCollection(dumpManager);
                    System.exit(0);
                } else {
                    logger.warn("Неизвестная команда: {}", input);
                }
            }
        }).start();
    }
}
