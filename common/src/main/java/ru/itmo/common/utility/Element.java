package ru.itmo.common.utility;

import java.io.Serializable;

/**
 * Абстрактный класс Element реализует интерфейсы Comparable, Validatable и Serializable.
 */
public abstract class Element<T> implements Comparable<T>, Validatable, Serializable {
    /**
     * Возвращает идентификатор элемента.
     *
     * @return Идентификатор элемента.
     */
    abstract public Long getId();
    abstract public void setId(Long id);
    /**
     * Возвращает имя элемента.
     *
     * @return Имя элемента.
     */
    abstract public String getName();

    public int compareTo(Element element) {
        return Long.compare(this.getId(), element.getId());
    }
}

