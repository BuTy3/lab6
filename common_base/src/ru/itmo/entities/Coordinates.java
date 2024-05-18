package ru.itmo.entities;

import ru.itmo.utility.Validatable;
import java.io.Serializable;

public class Coordinates implements Validatable, Serializable {
    private long x; //Максимальное значение поля: 964
    private Integer y; //Максимальное значение поля: 751, Поле не может быть null

    /**
     * Создает новый объект координат с указанными значениями x и y.
     *
     * @param x Координата x.
     * @param y Координата y.
     */
    public Coordinates(long x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Возвращает строковое представление координат в формате "x;y".
     *
     * @return Строковое представление координат.
     */
    @Override
    public String toString() {
        return x + ";" + y;
    }

    /**
     * Проверяет валидность координат.
     *
     * @return true, если координаты являются валидными, в противном случае - false.
     */
    @Override
    public boolean validate() {
        if (y == null) {
            return false;
        }
        if (x > 964) {
            return false;
        }
        if (y > 751) {
            return false;
        }
        return true;
    }
}