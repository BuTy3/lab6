package ru.itmo.client.network;

import ru.itmo.common.io.Console;
import ru.itmo.common.network.Answer;
import ru.itmo.common.network.Request;

import java.io.IOException;
import java.net.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Класс ClientHandler отвечает за управление соединением с сервером и отправку запросов.
 */
public class ClientHandler {
    private final String host;
    private final int port;
    private final Console console;
    private DatagramSocket socket;

    public ClientHandler(String host, int port, Console console) {
        this.host = host;
        this.port = port;
        this.console = console;
        initializeConnection();
    }

    /**
     * Инициализирует соединение с сервером.
     */
    private void initializeConnection() {
        try {
            socket = new DatagramSocket();
            socket.connect(new InetSocketAddress(host, port));
        } catch (IOException e) {
            console.printError("Error initializing connection: " + e.getMessage());
        }
    }

    /**
     * Отправляет запрос на сервер и ожидает ответа.
     *
     * @param request запрос для отправки
     * @return ответ от сервера
     */
    public Answer sendRequest(Request request) {
        try {
            boolean requestSent = false;
            while (true) {
                if (!requestSent) {
                    writeRequest(request);
                    requestSent = true;
                }
                Answer response = readResponse();
                if (response != null) {
                    return response;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            //console.printError("Error communicating with the server: " + e.getMessage());
            return new Answer(false, "Error communicating with the server: " + e.getMessage());
        }
    }

    /**
     * Отправляет запрос на сервер.
     *
     * @param request запрос для отправки
     * @throws IOException если возникает ошибка при отправке запроса
     */
    private void writeRequest(Request request) throws IOException {
        ClientWriter writer = new ClientWriter(socket, new InetSocketAddress(host, port));
        writer.send(request);
    }

    /**
     * Читает ответ от сервера.
     *
     * @return ответ от сервера
     * @throws IOException если возникает ошибка при чтении ответа
     * @throws ClassNotFoundException если полученный объект не является экземпляром класса Answer
     */
    private Answer readResponse() throws IOException, ClassNotFoundException {
        ClientReader reader = new ClientReader(socket);
        reader.read();
        Object receivedObject = reader.getReceivedObject();
        if (receivedObject instanceof Answer) {
            return (Answer) receivedObject;
        } else {
            throw new ClassNotFoundException("Полученный объект не является экземпляром Answer.");
        }
    }

    public void closeConnection() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}
