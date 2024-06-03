package ru.itmo.server.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.CRC32;

public class ServerReader {
    private static final Logger logger = LoggerFactory.getLogger(ServerReader.class);

    private final DatagramSocket socket;
    private final Map<Integer, byte[]> receivedPackets = new HashMap<>();
    private final Set<Integer> missingPackets = new HashSet<>();
    private int totalPackets = -1;
    private Object receivedObject;
    private SocketAddress clientAddress;

    public ServerReader(DatagramSocket socket) {
        this.socket = socket;
    }

    public SocketAddress read() throws IOException, ClassNotFoundException {
        long startTime = System.currentTimeMillis();
        while (true) {
            byte[] buffer = new byte[2048];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            try {
                socket.receive(packet);
            } catch (SocketTimeoutException e) {
                break;
            }

            clientAddress = packet.getSocketAddress();

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());
            DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

            while (dataInputStream.available() > 20) {
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
            requestMissingPackets();
        }
        return clientAddress;
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

    public boolean isRequestComplete() {
        return missingPackets.isEmpty() && receivedPackets.size() == totalPackets;
    }

    public Object getReceivedObject() {
        return receivedObject;
    }
}
