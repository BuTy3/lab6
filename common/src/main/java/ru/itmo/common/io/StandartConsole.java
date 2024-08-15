package ru.itmo.common.io;

import lombok.SneakyThrows;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Обеспечивает ввод команд и вывод результатов в стандартной консоли.
 *
 */
public class StandartConsole implements Console {
    private static final String PROMPT = "> ";
    private static List<OutputStream> streams = new ArrayList<>();
    private static OutputStream lastStream = null;

    /**
     * Выводит объект в консоль.
     *
     * @param obj Объект для печати.
     */
    public void print(Object obj) {
        System.out.print(obj);
    }

    /**
     * Выводит объект в консоль с переводом строки.
     *
     * @param obj Объект для печати.
     */
    public void println(Object obj) {
        System.out.println(obj);
    }

    public void println() {
        System.out.println();
    }

    /**
     * Выводит ошибку в консоль.
     *
     * @param obj Ошибка для печати.
     */
    @SneakyThrows
    public void printError(Object obj) {
        System.err.println("Error:" + obj);
    }

    /**
     * Выводит два элемента в формате таблицы.
     *
     * @param elementLeft  Левый элемент колонки.
     * @param elementRight Правый элемент колонки.
     */
    public void printTable(Object elementLeft, Object elementRight) {
        System.out.printf(" %-35s%-1s%n", elementLeft, elementRight);
    }

    /**
     * Выводит приглашение для ввода команды.
     */
    public void prompt() {
        print(PROMPT);
    }

    /**
     * Перенаправляет стандартный поток ошибок (System.err) на указанный OutputStream.
     *
     * @param stream OutputStream, на который должен быть перенаправлен стандартный поток ошибок.
     */
    public void setErr(OutputStream stream) {
        System.setErr(new PrintStream(new FixedStream(stream)));
    }

    /**
     * Перенаправляет стандартный поток вывода (System.out) на указанный OutputStream.
     *
     * @param stream OutputStream, на который должен быть перенаправлен стандартный поток вывода.
     */
    public void setOut(OutputStream stream) {
        System.setOut(new PrintStream(new FixedStream(stream)));
    }

    /**
     * Возвращает приглашение для ввода команды.
     *
     * @return Приглашение для ввода команды.
     */
    public String getPrompt() {
        return PROMPT;
    }

    private static class FixedStream extends OutputStream {
        private final OutputStream target;

        /**
         * Конструктор FixedStream инициализирует обернутый поток вывода и добавляет текущий экземпляр в список потоков.
         *
         * @param originalStream оригинальный поток вывода, который будет обернут в FixedStream.
         */
        public FixedStream(OutputStream originalStream) {
            target = originalStream;
            streams.add(this);
        }

        /**
         * Переопределение метода write(int b) из класса OutputStream.
         * Записывает один байт в поток вывода.
         *
         * @param b байт для записи в поток вывода.
         * @throws IOException если произошла ошибка ввода-вывода при записи в поток вывода.
         */
        @Override
        public void write(int b) throws IOException {
            // Проверяем, был ли уже использован текущий поток вывода
            if (lastStream != this) {
                // Если нет, выполняем операцию переключения потока вывода
                swap();
            }
            // Записываем один байт в целевой поток вывода
            target.write(b);
        }

        /**
         * Переопределение метода write(byte[] b) из класса OutputStream.
         * Записывает массив байтов в поток вывода.
         *
         * @param b массив байтов для записи в поток вывода.
         * @throws IOException если произошла ошибка ввода-вывода при записи в поток вывода.
         */
        @Override
        public void write(byte[] b) throws IOException {
            // Проверяем, был ли уже использован текущий поток вывода
            if (lastStream != this) {
                // Если нет, выполняем операцию переключения потока вывода
                swap();
            }
            // Записываем массив байтов в целевой поток вывода
            target.write(b);
        }

        /**
         * Переопределение метода write(byte[] b, int off, int len) из класса OutputStream.
         * Записывает часть массива байтов в поток вывода.
         *
         * @param b массив байтов для записи в поток вывода.
         * @param off смещение в массиве, с которого начинается чтение байтов.
         * @param len количество байтов для записи.
         * @throws IOException если произошла ошибка ввода-вывода при записи в поток вывода.
         */
        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            // Проверяем, был ли уже использован текущий поток вывода
            if (lastStream != this) {
                // Если нет, выполняем операцию переключения потока вывода
                swap();
            }
            // Записываем часть массива байтов в целевой поток вывода
            target.write(b, off, len);
        }

        /**
         * Метод swap() выполняет операцию переключения между текущим и предыдущим потоками вывода.
         *
         * @throws IOException если произошла ошибка ввода-вывода при сбросе предыдущего потока вывода.
         */
        private void swap() throws IOException {
            if (lastStream != null) {
                lastStream.flush();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
            }
            lastStream = this;
        }

        @Override
        public void close() throws IOException {
            target.close();
        }

        @Override
        public void flush() throws IOException {
            target.flush();
        }
    }
}

