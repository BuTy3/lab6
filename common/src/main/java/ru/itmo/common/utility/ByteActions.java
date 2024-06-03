package ru.itmo.common.utility;

import ru.itmo.common.network.Answer;
import ru.itmo.common.network.Networkable;

import java.io.*;
import java.nio.ByteBuffer;

public class ByteActions {

    // Метод для преобразования Answer в один ByteBuffer
    public static ByteBuffer toByteBuffer(Answer response) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(response);
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сериализации ответа", e);
        }

        ByteBuffer buffer = ByteBuffer.allocate(baos.size());
        buffer.put(baos.toByteArray());
        buffer.flip();

        return buffer;
    }

    // Метод для разделения ByteBuffer на несколько буферов заданного размера
    public static ByteBuffer[] split(ByteBuffer src, int unitSize) {
        int totalSize = src.remaining();
        int numBuffers = (int) Math.ceil((double) totalSize / unitSize);
        ByteBuffer[] buffers = new ByteBuffer[numBuffers];

        for (int i = 0; i < numBuffers; i++) {
            int bufferSize = Math.min(unitSize, src.remaining());
            byte[] chunk = new byte[bufferSize];
            src.get(chunk);
            buffers[i] = ByteBuffer.wrap(chunk);
        }

        return buffers;
    }
    // Метод для преобразования ByteBuffer обратно в Networkable
    public static Networkable fromByteBuffer(ByteBuffer buffer) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
             ObjectInputStream ois = new ObjectInputStream(bais)) {

            return (Networkable) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Ошибка при десериализации объекта", e);
        }
    }
}
