package ru.itmo.client.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.zip.CRC32;

/**
 * Класс ClientWriter отвечает за отправку объектов через DatagramSocket, разбивая их на пакеты.
 */
public class ClientWriter {
    private static final int PACKET_SIZE = 2024;
    private final DatagramSocket socket;
    private final SocketAddress serverAddress;

    public ClientWriter(DatagramSocket socket, SocketAddress serverAddress) {
        this.socket = socket;
        this.serverAddress = serverAddress;
    }

    /**
     * Отправляет объект данных, разбивая его на пакеты.
     *
     * @param data Объект данных, который нужно отправить.
     * @throws IOException если возникает ошибка ввода-вывода при отправке данных.
     */
    public void send(Object data) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(data);
        objectOutputStream.flush();
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        int totalPackets = (int) Math.ceil(byteArray.length / (double) PACKET_SIZE); //кол-во необходимых пакетов

        for (int i = 0; i < totalPackets; i++) {
            int start = i * PACKET_SIZE;
            int length = Math.min(byteArray.length - start, PACKET_SIZE);
            byte[] packetData = new byte[length];
            System.arraycopy(byteArray, start, packetData, 0, length);

            CRC32 crc = new CRC32();
            crc.update(packetData);
            long checksum = crc.getValue();

            ByteArrayOutputStream packetOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(packetOutputStream);

            dataOutputStream.writeInt(i);//номер пакета
            dataOutputStream.writeInt(totalPackets); //количество пакетов
            dataOutputStream.writeLong(checksum); //чексумма
            dataOutputStream.writeInt(length); //длинна
            dataOutputStream.write(packetData); //данные

            byte[] packetBytes = packetOutputStream.toByteArray();
            DatagramPacket packet = new DatagramPacket(packetBytes, packetBytes.length, serverAddress);
            socket.send(packet);
        }
    }
}
