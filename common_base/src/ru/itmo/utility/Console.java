package ru.itmo.utility;

/**
 * Интерфейс Console представляет методы для работы с консолью.
 */
public interface Console {
    /**
     * Выводит объект в консоль.
     *
     * @param obj Объект для вывода.
     */
    void print(Object obj);

    /**
     * Выводит объект в консоль с переводом строки.
     *
     * @param obj Объект для вывода.
     */
    void println(Object obj);

    /**
     * Выводит ошибку в консоль.
     *
     * @param callingClass Класс, вызвавший ошибку.
     * @param obj          Ошибка для вывода.
     */
    void printError(Class<?> callingClass, Object obj);

    /**
     * Выводит два элемента в формате таблицы.
     *
     * @param obj1 Первый элемент.
     * @param obj2 Второй элемент.
     */
    void printTable(Object obj1, Object obj2);

    /**
     * Выводит приглашение для ввода команды.
     */
    void prompt();

    /**
     * Возвращает приглашение для ввода команды.
     *
     * @return Приглашение для ввода команды.
     */
    String getPrompt();

    /**
     * Выводит пустую строку в консоль.
     */
    void println();
}

