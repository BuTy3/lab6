package ru.itmo.main;

import java.net.*;

public class Client {
    public static void main(String[] args) {
        try {
            // Создаем сокет для отправки на сервер на localhost, порт 12345
            DatagramSocket clientSocket = new DatagramSocket();

            // Получаем IP-адрес сервера
            InetAddress serverAddress = InetAddress.getByName("localhost");

            // Создаем сообщение для отправки
            String message = "Привет, сервер!";

            // Преобразуем сообщение в байтовый массив
            byte[] sendData = message.getBytes();

            // Создаем пакет для отправки данных на сервер
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, 12345);

            // Отправляем пакет на сервер
            clientSocket.send(sendPacket);

            System.out.println("Сообщение отправлено серверу: " + message);

            // Закрываем сокет клиента
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
