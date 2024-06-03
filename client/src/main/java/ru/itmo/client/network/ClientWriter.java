package ru.itmo.client.network;

import java.io.*;
import java.net.*;
import java.util.zip.CRC32;

public class ClientWriter {
    private final DatagramSocket socket;
    private final SocketAddress serverAddress;
    private static final int PACKET_SIZE = 2024;

    public ClientWriter(DatagramSocket socket, SocketAddress serverAddress) {
        this.socket = socket;
        this.serverAddress = serverAddress;
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
            DatagramPacket packet = new DatagramPacket(packetBytes, packetBytes.length, serverAddress);
            socket.send(packet);
        }
    }
}
