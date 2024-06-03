package ru.itmo.common.entities;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

public enum Semester implements Serializable {
    SECOND,
    THIRD,
    FOURTH,
    FIFTH,
    SIXTH;

    /**
     * @return Строка со всеми элементами enum'а через запятую.
     */
    public static String names() {
        return Arrays.stream(values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}
