package ru.itmo.server.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.common.entities.User;
import ru.itmo.common.managers.CommandManager;
import ru.itmo.common.network.Answer;
import ru.itmo.common.network.Request;
import ru.itmo.server.dao.UserDAO;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

/**
 * Класс, представляющий серверную часть приложения.
 * Он отвечает за прием запросов от клиентов и отправку ответов.
 */
public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private static final ExecutorService handlePool = new ForkJoinPool();
    private final int port;
    private DatagramSocket socket;
    private UserDAO userDAO = new UserDAO();

    public Server(int port) {
        this.port = port;
    }

    /**
     * Запускает сервер и начинает прослушивание входящих запросов.
     */
    public void start() {
        try {
            socket = new DatagramSocket(port);
            logger.info("Server started on port " + port);

            while (true) {
                try {
                    readRequest();
                } catch (IOException | ClassNotFoundException e) {
                    logger.error("Error reading request", e);
                }
            }
        } catch (IOException e) {
            logger.error("Error starting server", e);
        }
    }

    /**
     * Метод читает входящий запрос от клиента.
     *
     * @throws IOException            если произошла ошибка ввода-вывода
     * @throws ClassNotFoundException если класс объекта не найден при десериализации
     */
    private void readRequest() throws IOException, ClassNotFoundException {
        ServerReader reader = new ServerReader(socket);
        SocketAddress clientAddress = reader.read();
        if (reader.isRequestComplete()) {
            // Передаем обработку запроса в handlePool
            logger.info("request complete - processing");
            handlePool.submit(() -> {
                try {
                    Request request = (Request) reader.getReceivedObject();
                    User user = userDAO.getUserByUsername(request.getLogin());
                    if (user == null && !"register".equals(request.getCommand()) && !"login".equals(request.getCommand()))
                    // чел не залогинен
                    {
                        sendUnauthorizedResponse(clientAddress);
                        return;
                    } else if (user != null && !userDAO.verifyUserPassword(user.getUsername(), request.getPassword())
                            && !("register".equals(request.getCommand()) || "login".equals(request.getCommand()))
                    )
                    // чел пытается выполнить команду с ложными данными для входа
                    {
                        sendUnauthorizedResponse(clientAddress);
                        return;
                    }
                    Answer answer = CommandManager.handle(request);

                    new Thread(() -> sendResponse(clientAddress, answer)).start();
                } catch (Exception e) {
                    logger.error("Error handling request", e);
                }
            });
        }
    }

    private void sendResponse(SocketAddress clientAddress, Answer answer) {
        try {
            ServerWriter writer = new ServerWriter(socket, clientAddress);
            writer.send(answer);
        } catch (IOException e) {
            logger.error("Error writing response", e);
        }
    }

    private void sendUnauthorizedResponse(SocketAddress address) {
        Answer answer = new Answer(false, "Вы не вошли в систему." + '\n' +
                "Введите register для регистрации или login для входа");
        sendResponse(address, answer);
    }
}
