package ru.itmo.common.entities;

import lombok.*;
import ru.itmo.common.utility.Validatable;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Класс, представляющий собой человека.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Person implements Validatable, Comparable<Person>, Serializable {
    @NonNull
    private String name; // Поле не может быть null, Строка не может быть пустой
    private LocalDateTime birthday; // Поле может быть null
    private long weight; // Значение поля должно быть больше 0
    @NonNull
    private String passportID; // Строка не может быть пустой, Длина строки не должна быть больше 24, Поле не может быть null
    @NonNull
    private Color eyeColor; // Поле не может быть null

    /**
     * Проверяет валидность данных о человеке.
     *
     * @return true, если данные о человеке валидны, иначе false.
     */
    @Override
    public boolean validate() {
        if (name.isEmpty()) {
            return false;
        }
        if (passportID.isEmpty() || passportID.length() > 24) {
            return false;
        }
        return weight > 0;
    }

    @Override
    public int compareTo(Person o) {
        return 0;
    }
}
