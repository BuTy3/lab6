package ru.itmo.server.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.zip.CRC32;

public class ServerWriter {
    private static final Logger logger = LoggerFactory.getLogger(ServerWriter.class);

    private final DatagramSocket socket;
    private final SocketAddress clientAddress;
    private static final int PACKET_SIZE = 1024;

    public ServerWriter(DatagramSocket socket, SocketAddress clientAddress) {
        this.socket = socket;
        this.clientAddress = clientAddress;
    }

    public void send(Object data) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(data);
        objectOutputStream.flush();

        byte[] byteArray = byteArrayOutputStream.toByteArray();
        int totalPackets = (int) Math.ceil(byteArray.length / (double) PACKET_SIZE);

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

            dataOutputStream.writeInt(i);
            dataOutputStream.writeInt(totalPackets);
            dataOutputStream.writeLong(checksum);
            dataOutputStream.writeInt(length);
            dataOutputStream.write(packetData);

            byte[] packetBytes = packetOutputStream.toByteArray();
            DatagramPacket packet = new DatagramPacket(packetBytes, packetBytes.length, clientAddress);
            socket.send(packet);
        }
    }
}
