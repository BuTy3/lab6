package ru.itmo.entities;

import java.io.Serializable;

public enum Color implements Serializable {
    BLACK,
    ORANGE,
    WHITE,
    BROWN;

    /**
     * @return Строка со всеми элементами enum'а через запятую.
     */
    public static String names() {
        StringBuilder nameList = new StringBuilder();
        for (Color color_type : values()) {
            nameList.append(color_type.name()).append(", ");
        }
        return nameList.substring(0, nameList.length()-2);
    }
}
