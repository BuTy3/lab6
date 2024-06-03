package ru.itmo.common.entities;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

public enum FormOfEducation implements Serializable {
    DISTANCE_EDUCATION,
    FULL_TIME_EDUCATION,
    EVENING_CLASSES;

    /**
     * @return Строка со всеми элементами enum'а через запятую.
     */
    public static String names() {
        return Arrays.stream(values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}
