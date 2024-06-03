package ru.itmo.common.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import ru.itmo.common.utility.Validatable;

import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Coordinates implements Validatable, Serializable {
    private static final long MAX_X = 964;
    private static final int MAX_Y = 751;

    private long x; //Максимальное значение поля: 964
    private Integer y; //Максимальное значение поля: 751, Поле не может быть null

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
        if (x > MAX_X) {
            return false;
        }
        if (y > MAX_Y) {
            return false;
        }
        return true;
    }
}
