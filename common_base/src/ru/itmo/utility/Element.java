package ru.itmo.utility;

import java.io.Serializable;

/**
 * Абстрактный класс Element реализует интерфейсы Comparable, Validatable и Serializable.
 */
public abstract class Element implements Comparable<Element>, Validatable, Serializable {
    /**
     * Возвращает идентификатор элемента.
     *
     * @return Идентификатор элемента.
     */
    abstract public int getId();

    /**
     * Возвращает имя элемента.
     *
     * @return Имя элемента.
     */
    abstract public String getName();
}

