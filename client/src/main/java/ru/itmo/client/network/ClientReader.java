package ru.itmo.client.network;

import java.io.*;
import java.net.*;
import java.util.*;
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

    public void read() throws IOException, ClassNotFoundException {
        long startTime = System.currentTimeMillis();
        byte[] buffer = new byte[2048];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        while (true) {

            try {
                socket.receive(packet);
            } catch (SocketTimeoutException e) {
                break;
            }

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
            DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

            while (dataInputStream.available() > 20) {
                int packetNumber = dataInputStream.readInt();
                int totalPackets = dataInputStream.readInt();
                long checksum = dataInputStream.readLong();
                int length = dataInputStream.readInt();
                byte[] data = new byte[length];
                dataInputStream.readFully(data);

                CRC32 crc = new CRC32(); //чек сумма полученных пакетов
                crc.update(data);
                if (crc.getValue() != checksum) {
                    missingPackets.add(packetNumber);
                    continue;
                }

                receivedPackets.put(packetNumber, data);

                if (this.totalPackets == -1) {
                    this.totalPackets = totalPackets;
                }

                for (int i = 0; i < totalPackets; i++) {
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

    private void reassembleObject() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i = 0; i < totalPackets; i++) {
            byteArrayOutputStream.write(receivedPackets.get(i));
        }

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        receivedObject = objectInputStream.readObject();
    }

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

    public Object getReceivedObject() {
        return receivedObject;
    }
}
