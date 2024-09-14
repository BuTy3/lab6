package ru.itmo.client.network;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.CRC32;

public class ClientReader {
    private final DatagramSocket socket;
    private final Map<Integer, byte[]> receivedPackets = new HashMap<>();
    private final Set<Integer> missingPackets = new HashSet<>();
    private int totalPackets = -1;
    private Object receivedObject;

    public ClientReader(DatagramSocket socket) {
        this.socket = socket;
    }

    /**
     * Читает и собирает данные, полученные по UDP сокету, с проверкой целостности и повторным запросом
     * недостающих пакетов при необходимости.
     *
     * @throws IOException            если возникает ошибка ввода-вывода при получении данных.
     * @throws ClassNotFoundException если возникла ошибка при реконструкции объекта.
     */
    public void read() throws IOException, ClassNotFoundException {
        long startTime = System.currentTimeMillis();
        byte[] buffer = new byte[2048];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        //получение данных
        while (true) {

            try {
                socket.receive(packet);
            } catch (SocketTimeoutException e) {
                break;
            }

            //чтение данных
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
            DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);


            while (dataInputStream.available() > 20) {
                int packetNumber = dataInputStream.readInt(); //номер пакета
                int totalPackets = dataInputStream.readInt(); //всего пакетов
                long checksum = dataInputStream.readLong();  //чексумма
                int length = dataInputStream.readInt(); //длинна
                byte[] data = new byte[length]; //получение данных в массив
                dataInputStream.readFully(data); //запись в поток

                CRC32 crc = new CRC32(); //чек сумма полученных пакетов
                crc.update(data);
                if (crc.getValue() != checksum) {
                    missingPackets.add(packetNumber); //добавка в потеряшки
                    continue;
                }

                receivedPackets.put(packetNumber, data); //добавляет в полученные данные

                if (this.totalPackets == -1) {
                    this.totalPackets = totalPackets;
                }

                for (int i = 0; i < totalPackets; i++) { //чек на потеряшек
                    if (!receivedPackets.containsKey(i)) {
                        missingPackets.add(i);
                    } else {
                        missingPackets.remove(i);
                    }
                }
            }

            if (missingPackets.isEmpty() && receivedPackets.size() == totalPackets) {
                break;
            }

            if (System.currentTimeMillis() - startTime > 1000) {
                break;
            }
        }

        if (missingPackets.isEmpty() && receivedPackets.size() == totalPackets) {
            reassembleObject();
        } else {
            requestMissingPackets(packet.getSocketAddress());
        }
    }

    /**
     * Собирает объект из полученных пакетов данных.
     *
     * @throws IOException            если возникает ошибка ввода-вывода при сборке объекта.
     * @throws ClassNotFoundException если класс объекта не найден при десериализации.
     */
    private void reassembleObject() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i = 0; i < totalPackets; i++) {
            byteArrayOutputStream.write(receivedPackets.get(i)); //объеденяем данные из пакетов
        }

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        receivedObject = objectInputStream.readObject(); //десериализованные объект
    }

    /**
     * Запрашивает недостающие пакеты у сервера.
     *
     * @param serverAddress адрес сервера для повторного запроса недостающих пакетов.
     * @throws IOException если возникает ошибка ввода-вывода при отправке запроса.
     */
    private void requestMissingPackets(SocketAddress serverAddress) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        dataOutputStream.writeInt(missingPackets.size());
        for (Integer packetNumber : missingPackets) {
            dataOutputStream.writeInt(packetNumber);
        }

        byte[] requestData = byteArrayOutputStream.toByteArray();
        DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length, serverAddress);
        socket.send(requestPacket);
    }

    /**
     * Возвращает полученный объект.
     *
     * @return десериализованный объект, полученный из пакетов данных.
     */
    public Object getReceivedObject() {
        return receivedObject;
    }
}
