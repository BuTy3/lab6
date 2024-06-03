package ru.itmo.client.runtime;

import ru.itmo.common.commands.Command;
import ru.itmo.common.exception.ScriptRecursionException;
import ru.itmo.common.io.*;
import ru.itmo.common.managers.CommandManager;
import ru.itmo.common.network.*;
import ru.itmo.client.network.ClientHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Runner {
    public enum ExitCode {
        OK,
        ERROR,
        EXIT,
    }

    private final Console console;
    private final ClientHandler clientHandler;
    private final List<String> commandHistory = new ArrayList<>();
    private final List<String> scriptStack = new ArrayList<>();
    private final CommandManager commandManager;

    public Runner(ClientHandler clientHandler, Console console) {
        this.clientHandler = clientHandler;
        this.console = console;
        this.commandManager = new CommandManager();
    }

    /**
     * Интерактивный режим
     */
    public void interactiveMode() {
        var userScanner = InputSteamer.getScanner();
        try {
            ExitCode commandStatus;
            String[] userCommand = {"", ""};

            do {
                console.prompt();
                try {
                    userCommand = (userScanner.nextLine().trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();

                    commandHistory.add(userCommand[0]);
                    commandStatus = launchCommand(userCommand);
                } catch (NoSuchElementException exception) {
                    console.printError("Пользовательский ввод не обнаружен! Попытка автоматического завершения работы...");
                    commandStatus = launchCommand(new String[]{"exit", ""});
                }
            } while (commandStatus != ExitCode.EXIT);

        } catch (IllegalStateException exception) {
            console.printError("Непредвиденная ошибка!");
        } catch (Exception exception) {
            console.printError("Произошла ошибка: " + exception.getMessage());
        }
    }

    /**
     * Режим для запуска скрипта.
     *
     * @param argument Аргумент скрипта
     * @return Код завершения.
     */
    public ExitCode scriptMode(String argument) {
        String[] userCommand = {"", ""};
        ExitCode commandStatus;
        scriptStack.add(argument);
        if (!new File(argument).exists()) {
            argument = "../" + argument;
        }
        try (Scanner scriptScanner = new Scanner(new File(argument))) {
            if (!scriptScanner.hasNext()) throw new NoSuchElementException();
            Scanner tmpScanner = InputSteamer.getScanner();
            InputSteamer.setScanner(scriptScanner);
            InputSteamer.setFileMode(true);

            do {
                do {
                    userCommand = (scriptScanner.nextLine().trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                } while (scriptScanner.hasNextLine() && userCommand[0].isEmpty());
                console.println(console.getPrompt() + String.join(" ", userCommand));
                if (userCommand[0].equals("execute_script")) {
                    for (String script : scriptStack) {
                        if (userCommand[1].equals(script)) throw new ScriptRecursionException();
                    }
                }
                commandStatus = launchCommand(userCommand);
            } while (commandStatus == ExitCode.OK && scriptScanner.hasNextLine());

            InputSteamer.setScanner(tmpScanner);
            InputSteamer.setFileMode(false);

            if (commandStatus == ExitCode.ERROR && !(userCommand[0].equals("execute_script") && !userCommand[1].isEmpty())) {
                console.println("Проверьте скрипт на корректность введенных данных!");
            }

            return commandStatus;

        } catch (FileNotFoundException exception) {
            console.printError("Файл со скриптом не найден!");
        } catch (NoSuchElementException exception) {
            console.printError("Файл со скриптом пуст!");
        } catch (ScriptRecursionException exception) {
            console.printError("Скрипты не могут вызываться рекурсивно!");
        } catch (IllegalStateException exception) {
            console.printError("Непредвиденная ошибка!");
            System.exit(0);
        } finally {
            scriptStack.remove(scriptStack.size() - 1);
        }
        return ExitCode.ERROR;
    }

    /**
     * @param userCommand Команда для запуска
     * @return Код завершения.
     */
    private ExitCode launchCommand(String[] userCommand) {
        if (userCommand[0].isEmpty()) return ExitCode.OK;
        if(userCommand[0].equals("execute_script")){
            return scriptMode(userCommand[1]);
        }
        Answer response = null;
        try {
            Command command = commandManager.getCommands().get(userCommand[0]);
            Request request = (command == null) ? null : command.execute(userCommand);
            if (request != null) {
                if (request.getCommand().equals("help")) {
                    console.println(request.getData());
                    return ExitCode.OK;
                }
                if (request.getCommand().equals("exit")) {
                    console.println(request.getData());
                    return ExitCode.EXIT;
                }
                response = clientHandler.sendRequest(request);
                if (response.isSuccess()) {
                    console.println(response.toString());
                } else {
                    console.printError(response.toString());
                }
            } else {
                console.printError("Команда не инициализирована!");
                return ExitCode.ERROR;
            }
        } catch (Exception exception) {
            console.printError("Ошибка выполнения команды: " + exception.getMessage());
            return ExitCode.ERROR;
        }

        return ExitCode.OK;
    }
}
