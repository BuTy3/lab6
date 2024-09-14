package ru.itmo.common.commands;

import ru.itmo.common.entities.User;
import ru.itmo.common.exception.InvalidFormException;
import ru.itmo.common.exception.InvalidNumberOfElementsException;
import ru.itmo.common.network.Answer;
import ru.itmo.common.network.Request;
import ru.itmo.common.utility.InputParser;
import ru.itmo.common.utility.UserService;

import javax.management.InstanceAlreadyExistsException;
import java.util.Arrays;

/**
 * Command 'register'. Registers a new user in the system.
 */
public class Register extends Command {
    public static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_USERNAME_LENGTH = 50;
    private UserService userDAO;

    public Register() {
        super(CommandName.REGISTER, "register a new user");
    }

    /**
     * Constructor for creating an instance of the Register command.
     *
     * @param userDAO the user manager
     */
    public Register(UserService userDAO) {
        this();
        this.userDAO = userDAO;
    }

    /**
     * Executes the command.
     *
     * @param request the request to register a user
     * @return the response indicating the success or failure of the command execution
     */
    @Override
    public Answer execute(Request request) {
        try {
            if (request.getLogin().length() >= MAX_USERNAME_LENGTH)
                throw new InvalidFormException("Username length must be less than " + MAX_USERNAME_LENGTH);

            if (request.getPassword().length() < MIN_PASSWORD_LENGTH)
                throw new InvalidFormException("Password length must be at least " + MIN_PASSWORD_LENGTH);

            if (request.getUserId() != null) throw new InstanceAlreadyExistsException("User already exists");

            var user = userDAO.insertUser(request.getLogin(), request.getPassword());

            if (user == null) throw new InstanceAlreadyExistsException("User already exists");

            if (!user.validate())
                throw new InvalidFormException("User not registered, user fields are not valid!");

            return new Answer(true, "User successfully registered", user.getId());
        } catch (InstanceAlreadyExistsException ex) {
            return new Answer(false, ex.getMessage(), null);
        } catch (InvalidFormException invalid) {
            return new Answer(false, invalid.getMessage());
        } catch (Exception e) {
            return new Answer(false, e.toString(), -1);
        }
    }

    /**
     * Executes the command.
     *
     * @param arguments the command arguments (expects the username and password)
     * @return the request indicating the success or failure of the command execution
     */
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
