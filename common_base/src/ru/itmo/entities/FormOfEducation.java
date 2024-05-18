package ru.itmo.entities;

public enum FormOfEducation implements java.io.Serializable {
    DISTANCE_EDUCATION,
    FULL_TIME_EDUCATION,
    EVENING_CLASSES;

    /**
     * @return Строка со всеми элементами enum'а через запятую.
     */
    public static String names() {
        StringBuilder nameList = new StringBuilder();
        for (FormOfEducation ford_of_ed : values()) {
            nameList.append(ford_of_ed.name()).append(", ");
        }
        return nameList.substring(0, nameList.length()-2);
    }
}
