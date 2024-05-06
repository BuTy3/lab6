package ru.itmo.main;

import java.net.*;

public class Server {
    public static void main(String[] args) {
        try {
            // Создаем сокет для прослушивания по порту 12345
            DatagramSocket serverSocket = new DatagramSocket(12345);

            // Создаем буфер для приема данных
            byte[] receiveData = new byte[1024];

            System.out.println("Сервер запущен. Ожидание сообщений от клиента...");

            while (true) {
                // Создаем пакет для приема данных
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                // Принимаем пакет от клиента
                serverSocket.receive(receivePacket);

                // Извлекаем данные из пакета
                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());

                // Выводим полученное сообщение
                System.out.println("Получено от клиента: " + message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
