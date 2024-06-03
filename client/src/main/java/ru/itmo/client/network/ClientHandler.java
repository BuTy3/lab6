package ru.itmo.client.network;

import ru.itmo.common.io.Console;
import ru.itmo.common.network.Answer;
import ru.itmo.common.network.Request;

import java.io.IOException;
import java.net.*;
import java.util.Iterator;
import java.util.Set;

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

    private void initializeConnection() {
        try {
            socket = new DatagramSocket();
            socket.connect(new InetSocketAddress(host, port));
        } catch (IOException e) {
            console.printError("Error initializing connection: " + e.getMessage());
        }
    }

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

    private void writeRequest(Request request) throws IOException {
        ClientWriter writer = new ClientWriter(socket, new InetSocketAddress(host, port));
        writer.send(request);
    }

    private Answer readResponse() throws IOException, ClassNotFoundException {
        ClientReader reader = new ClientReader(socket);
        reader.read();
        Object receivedObject = reader.getReceivedObject();
        if (receivedObject instanceof Answer) {
            return (Answer) receivedObject;
        } else {
            throw new ClassNotFoundException("Received object is not an instance of Answer.");
        }
    }

    public void closeConnection() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}
