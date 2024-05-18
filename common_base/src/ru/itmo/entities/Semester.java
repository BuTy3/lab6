package ru.itmo.entities;

public enum Semester {
    SECOND,
    THIRD,
    FOURTH,
    FIFTH,
    SIXTH;

    /**
     * @return Строка со всеми элементами enum'а через запятую.
     */
    public static String names() {
        StringBuilder nameList = new StringBuilder();
        for (Semester quantity_sem : values()) {
            nameList.append(quantity_sem.name()).append(", ");
        }
        return nameList.substring(0, nameList.length()-2);
    }
}
