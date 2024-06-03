package ru.itmo.server.main;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.common.entities.StudyGroup;
import ru.itmo.common.io.Console;
import ru.itmo.common.io.StandartConsole;
import ru.itmo.common.managers.CommandManager;
import ru.itmo.server.managers.DumpManager;
import ru.itmo.server.managers.StudyGroupCollectionManager;
import ru.itmo.server.network.Server;

import java.io.File;
import java.util.Scanner;

/**
 *
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final Console console = new StandartConsole();
    private static final int PORT = 23982;

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

        DumpManager<StudyGroup> dumpManager = new DumpManager<>(filePath, StudyGroup.class);
        StudyGroupCollectionManager groupCollection = new StudyGroupCollectionManager();
        groupCollection.loadCollection(dumpManager);

        addShutdownHook(groupCollection, dumpManager);

        CommandManager.initServerCommands(groupCollection);
        Server server = new Server(PORT);
        server.start();
    }

    private static void addShutdownHook(StudyGroupCollectionManager collectionManager, DumpManager<StudyGroup> dumpManager) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Сохранение коллекции перед завершением работы...");
            collectionManager.saveCollection(dumpManager);
        }));
    }

    private static void startConsoleListener(StudyGroupCollectionManager collectionManager, DumpManager<StudyGroup> dumpManager) {
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String input = scanner.nextLine().trim();
                if ("exit".equalsIgnoreCase(input)) {
                    logger.info("Завершение работы программы...");
                    collectionManager.saveCollection(dumpManager);
                    System.exit(0);
                } else if ("save".equalsIgnoreCase(input)) {
                    logger.info("Сохранение коллекции...");
                    collectionManager.saveCollection(dumpManager);
                } else {
                    logger.warn("Неизвестная команда: {}", input);
                }
            }
        }).start();
    }
}
