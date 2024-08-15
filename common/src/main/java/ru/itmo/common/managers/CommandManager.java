package ru.itmo.common.managers;

import lombok.Getter;
import ru.itmo.common.commands.*;
import ru.itmo.common.entities.forms.Form;
import ru.itmo.common.io.Console;
import ru.itmo.common.entities.StudyGroup;
import ru.itmo.common.network.Answer;
import ru.itmo.common.network.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {
    /**
     *  Получает словарь команд.
     */
    @Getter
    private static Map<String, Command> commands;
    /**
     *  Получает историю команд.
     */
    private static final List<String> commandHistory = new ArrayList<>();

    /**
     * Регистрирует команду.
     *
     * @param commandName Название команды.
     * @param command     Команда
     */
    public static void register(String commandName, Command command) {
        commands.put(commandName, command);
    }
    public static void init(){
        commands = new HashMap<>();
        register("exit", new Exit());
    }

    public static Map<String, Command> getCommands() {return commands;}
    public static void initServerCommands(CollectionManager<StudyGroup> groupCollectionManager){
        init();
        register("show", new Show(groupCollectionManager));
        register("add", new Add(groupCollectionManager));
        register("clear", new Clear(groupCollectionManager));
        register("info", new Info(groupCollectionManager));
        register("remove_by_at", new RemoveAt(groupCollectionManager));
        register("remove_by_id", new Remove_by_id(groupCollectionManager));
        register("update", new Update(groupCollectionManager));
        register("remove_all_by_students_count", new RemoveAllByStudentsCount(groupCollectionManager));
        register("filter_starts_with_name", new FilterStartsWithName(groupCollectionManager));
        register("min_by_group_admin", new MinByGroupAdmin(groupCollectionManager));
        register("history", new History());
    }
    public static void initClientCommands(Console console, Form<StudyGroup> studyGroupForm){
        init();
        register("help", new Help());
        register("info", new Info());
        register("show", new Show());
        register("add", new Add(console, studyGroupForm));
        register("update", new Update(console, studyGroupForm));
        register("remove_by_id", new Remove_by_id());
        register("clear", new Clear());
        register("execute_script", new ExecuteScript());
        register("remove_by_at", new RemoveAt());
        register("filter_starts_with_name", new FilterStartsWithName());
        register("min_by_group_admin", new MinByGroupAdmin());
        register("remove_greater", new RemoveGreater());
        register("remove_all_by_students_count", new RemoveAllByStudentsCount());
        register("history", new History());
    }

    /**
     * Обрабатывает запрос и выполняет соответствующую команду.
     *
     * @param request запрос, содержащий команду для выполнения.
     * @return ответ на выполнение команды.
     */
    public static Answer handle(Request request) {
        var command = getCommands().get(request.getCommand());
        if (command == null) return new Answer(false, request.getCommand(), "Команда не найдена!");
        addToHistory(request.getCommand());
        if(!"exit".equals(request.getCommand()) && !"save".equals(request.getCommand())) {
            return command.execute(request);
        }
        return new Answer(false, "Неизвестная команда");
    }

    // Обработать команду, поступившую из консоли сервера
    public static void handleServer(Request request) {
        var command = getCommands().get(request.getCommand());
        if (command == null) return;
        //addToHistory(request.getCommand());
        if("exit".equals(request.getCommand()) || "save".equals(request.getCommand())) command.execute(request);
    }
    /**
     * Добавляет команду в историю.
     *
     * @param command Команда.
     */
    public static void addToHistory(String command) {
        commandHistory.add(command);
    }

    public static String help() {
        StringBuilder sb = new StringBuilder();
        sb.append("Command List:\n");
        commands.values().forEach(command -> {
            sb.append(String.format(" %-35s%-1s%n", command.getName(), command.getDescription()));
        });
        return sb.toString();
    }

    public static List<String> get_commandHistory(){
        return commandHistory;
    }
}
