package ru.itmo.common.utility;

import ru.itmo.common.entities.Color;
import ru.itmo.common.entities.FormOfEducation;
import ru.itmo.common.entities.Semester;
import ru.itmo.common.io.Console;
import ru.itmo.common.io.InputSteamer;
import ru.itmo.common.io.StandartConsole;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputParser {
    private final Console console = new StandartConsole();
    private Scanner scanner = (InputSteamer.getScanner() != null) ? InputSteamer.getScanner() : new Scanner(System.in);

    public String readString(String prompt) {
        console.print(prompt + ": ");
        return scanner.nextLine().trim();
    }

    public int readInt(String prompt) {
        while (true) {
            console.print(prompt + ": ");
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                console.printError("Ошибка: введено не целое число. Попробуйте снова.");
            }
        }
    }

    public long readLong(String prompt) {
        while (true) {
            console.print(prompt + ": ");
            String input = scanner.nextLine().trim();
            try {
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                console.printError("Ошибка: введено не целое число. Попробуйте снова.");
            }
        }
    }

    public LocalDateTime readLocalDateTime(String prompt) {
        while (true) {
            console.print(prompt + ": ");
            String input = scanner.nextLine().trim();
            try {
                return LocalDateTime.parse(input);
            } catch (DateTimeParseException e) {
                console.printError("Ошибка: введена неверная дата. Формат должен быть 'YYYY-MM-DDTHH:MM:SS'. Попробуйте снова.");
            }
        }
    }

    public Color readColor(String prompt) {
        while (true) {
            console.print(prompt + " (" + Color.names() + "): ");
            String input = scanner.nextLine().trim().toUpperCase();
            try {
                return Color.valueOf(input);
            } catch (IllegalArgumentException e) {
                console.printError("Ошибка: неверный цвет. Попробуйте снова.");
            }
        }
    }

    public FormOfEducation readFormOfEducation(String prompt) {
        while (true) {
            console.print(prompt + " (DISTANCE_EDUCATION, FULL_TIME_EDUCATION, EVENING_CLASSES): ");
            String input = scanner.nextLine().trim().toUpperCase();
            try {
                return FormOfEducation.valueOf(input);
            } catch (IllegalArgumentException e) {
                console.printError("Ошибка: неверная форма обучения. Попробуйте снова.");
            }
        }
    }

    public Semester readSemester(String prompt) {
        while (true) {
            console.print(prompt + " (FIRST, SECOND, THIRD, FOURTH, FIFTH, SIXTH, SEVENTH, EIGHTH): ");
            String input = scanner.nextLine().trim().toUpperCase();
            try {
                return Semester.valueOf(input);
            } catch (IllegalArgumentException e) {
                console.printError("Ошибка: неверный семестр. Попробуйте снова.");
            }
        }
    }
}
