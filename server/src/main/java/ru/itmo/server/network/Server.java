package ru.itmo.server.network;

import ru.itmo.common.managers.CommandManager;
import ru.itmo.common.network.Answer;
import ru.itmo.common.network.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketAddress;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private final int port;
    private DatagramSocket socket;

    public Server(int port) {
        this.port = port;
    }

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

    private void readRequest() throws IOException, ClassNotFoundException {
        ServerReader reader = new ServerReader(socket);
        SocketAddress clientAddress = reader.read();
        if (reader.isRequestComplete()) {
            Request request = (Request) reader.getReceivedObject();
            Answer answer = CommandManager.handle(request);
            sendResponse(clientAddress, answer);
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
}