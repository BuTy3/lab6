package ru.itmo.entities;

import ru.itmo.exception.InvalidFormException;
import ru.itmo.exception.InvalidScriptInputException;

public abstract class Form<T> {
    /**
     * Метод для построения объекта на основе введенных пользовательских данных.
     *
     * @return созданный объект
     * @throws InvalidScriptInputException если введены некорректные данные в скрипте
     * @throws InvalidFormException        если введены некорректные данные вручную
     */
    public abstract T build() throws InvalidScriptInputException, InvalidFormException;
}
