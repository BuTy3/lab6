package ru.itmo.server.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;
import java.util.zip.CRC32;

/**
 * Класс, представляющий собой читателя запросов на сервере.
 * Он отвечает за чтение входящих пакетов, их проверку, и сбор их в объект.
 */
public class ServerReader {
    private static final Logger logger = LoggerFactory.getLogger(ServerReader.class);

    private final DatagramSocket socket;
    private final Map<Integer, byte[]> receivedPackets = new HashMap<>();
    private final Set<Integer> missingPackets = new HashSet<>();
    private int totalPackets = -1;
    private Object receivedObject;
    private SocketAddress clientAddress;

    private final ForkJoinPool forkJoinPool = new ForkJoinPool();

    public ServerReader(DatagramSocket socket) {
        this.socket = socket;
    }

    /**
     * Метод для чтения входящего запроса от клиента.
     *
     * @return адрес клиента, с которого был получен запрос
     * @throws IOException            если произошла ошибка ввода-вывода
     * @throws ClassNotFoundException если класс объекта не найден при десериализации
     */
    public SocketAddress read() throws IOException, ClassNotFoundException {
        long startTime = System.currentTimeMillis();
        byte[] buffer = new byte[2048];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        try {
            socket.receive(packet);

            clientAddress = packet.getSocketAddress();
            logger.debug("Чтение запроса от: {}", clientAddress);

            // для чтения данных из пакета
            forkJoinPool.execute(new PacketHandler(packet));
        } catch (SocketTimeoutException e) {
        }

        forkJoinPool.shutdown();
        while (!forkJoinPool.isTerminated()) {
            // Ожидание завершения всех потоков
        }

        // Проверка завершенности запроса и сборка объекта
        if (missingPackets.isEmpty() && receivedPackets.size() == totalPackets) {
            reassembleObject();
        } else {
            requestMissingPackets();
        }
        return clientAddress;
    }

    /**
     * Класс, представляющий собой задачу для ForkJoinPool, которая обрабатывает пакет.
     */
    private class PacketHandler extends RecursiveAction {
        private final DatagramPacket packet;

        public PacketHandler(DatagramPacket packet) {
            this.packet = packet;
        }

        @Override
        protected void compute() {
            try {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
                DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

                while (dataInputStream.available() > 20) {
                    logger.debug("Обрабатываем пакет");
                    int packetNumber = dataInputStream.readInt();
                    int totalPackets = dataInputStream.readInt();
                    long checksum = dataInputStream.readLong();
                    int length = dataInputStream.readInt();
                    byte[] data = new byte[length];
                    dataInputStream.readFully(data);

                    CRC32 crc = new CRC32();
                    crc.update(data);
                    if (crc.getValue() != checksum) {
                        missingPackets.add(packetNumber);
                        logger.warn("Checksum does not match for packet: " + packetNumber);
                        continue;
                    }

                    synchronized (receivedPackets) {
                        receivedPackets.put(packetNumber, data);
                    }
                    logger.debug("da");
                    synchronized (ServerReader.this) {
                        if (ServerReader.this.totalPackets == -1) {
                            ServerReader.this.totalPackets = totalPackets;
                        }
                        logger.debug("total packets: " + ServerReader.this.totalPackets);
                        for (int i = 0; i < totalPackets; i++) {
                            if (!receivedPackets.containsKey(i)) {
                                missingPackets.add(i);
                            } else {
                                missingPackets.remove(i);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                logger.error("Ошибка обработки пакета", e);
            }
        }
    }

    /**
     * Метод собирает полученные пакеты в объект.
     *
     * @throws IOException            если произошла ошибка ввода-вывода
     * @throws ClassNotFoundException если класс объекта не найден при десериализации
     */
    private void reassembleObject() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();  // Создание потока для записи данных в байтовый массив
        for (int i = 0; i < totalPackets; i++) {
            byte[] packetData = receivedPackets.get(i);
            if (packetData != null) {
                byteArrayOutputStream.write(packetData);
            }
        }

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

        receivedObject = objectInputStream.readObject(); //десереализация
    }

    /**
     * Метод отправляет запрос на повторную отправку потерянных пакетов.
     *
     * @throws IOException если произошла ошибка ввода-вывода
     */
    private void requestMissingPackets() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        dataOutputStream.writeInt(missingPackets.size());
        for (Integer packetNumber : missingPackets) {
            dataOutputStream.writeInt(packetNumber);
        }

        byte[] requestData = byteArrayOutputStream.toByteArray();
        DatagramPacket requestPacket = new DatagramPacket(requestData, requestData.length, clientAddress);
        socket.send(requestPacket);
    }

    /**
     * Метод проверяет, завершен ли запрос.
     *
     * @return {@code true}, если запрос завершен, {@code false} в противном случае
     */
    public boolean isRequestComplete() {
        return missingPackets.isEmpty() && receivedPackets.size() == totalPackets;
    }

    /**
     * Метод возвращает объект, полученный в результате запроса.
     *
     * @return объект, полученный в результате запроса
     */
    public Object getReceivedObject() {
        return receivedObject;
    }
}
