package ru.itmo.common.commands;

import ru.itmo.common.entities.User;
import ru.itmo.common.exception.InvalidNumberOfElementsException;
import ru.itmo.common.network.Answer;
import ru.itmo.common.network.Request;
import ru.itmo.common.utility.InputParser;
import ru.itmo.common.utility.UserService;

/**
 * Command 'login'. Logs in a user to the system.
 */
public class Login extends Command {
    private UserService userDAO;

    public Login() {
        super(CommandName.LOGIN, " log in to the system");
    }

    public Login(UserService userDAO) {
        this();
        this.userDAO = userDAO;
    }

    @Override
    public Answer execute(Request request) {
        try {
            String username = request.getLogin();
            String password = request.getPassword();

            if (!userDAO.verifyUserPassword(username, password)) {
                return new Answer(false, "Invalid username or password", null);
            }

            User user = userDAO.getUserByUsername(username);

            if (user == null) {
                return new Answer(false, "User not found", null);
            }

            if (user.getId() == null) {
                return new Answer(false, "User ID is null", null);
            }

            return new Answer(true, "You have successfully logged in", user.getId());
        } catch (Exception e) {
            System.out.println("Exception during login: " + e); // Debug message
            return new Answer(false, e.toString(), null);
        }
    }

    @Override
    public Request execute(String[] arguments) {
        try {
            if (!arguments[1].isEmpty()) throw new InvalidNumberOfElementsException();

            Request request = new Request(true, getName(), null);
            String login, password;
            InputParser parser = new InputParser();
            login = parser.readString("Введите логин");
            password = parser.readString("Введите пароль");
            request.setLogin(login);
            request.setPassword(password);
            return request;
        } catch (InvalidNumberOfElementsException exception) {
            return new Request(false, getName(), getUsingError());
        }
    }
}
