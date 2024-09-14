package ru.itmo.client.main;

import ru.itmo.client.network.ClientHandler;
import ru.itmo.client.runtime.Runner;
import ru.itmo.common.entities.forms.StudyGroupForm;
import ru.itmo.common.io.InputSteamer;
import ru.itmo.common.io.StandartConsole;
import ru.itmo.common.managers.CommandManager;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Инициализация консоли
        StandartConsole console = new StandartConsole();
        console.setOut(System.out);
        console.setErr(System.err);
        InputSteamer.setScanner(new Scanner(System.in));
        // Параметры подключения
        String host = "localhost"; //scp -rP 2222 lab6 s412991@helios.cs.ifmo.ru:~/   export GROUPS_FILE_PATH=data/file.json
        int port = 4202;

        // Инициализация менеджера команд
        CommandManager.initClientCommands(console, new StudyGroupForm());
        // Инициализация клиентского обработчика
        ClientHandler clientHandler = new ClientHandler(host, port, console);

        // Инициализация и запуск Runner
        Runner runner = new Runner(clientHandler, console);
        runner.interactiveMode();
    }
}
