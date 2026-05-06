package be.jonasboon.book_keeping_tool.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;

@UtilityClass
public class BookYearUtils {

    public static LocalDate startOf(int bookYear) {
        return LocalDate.of(bookYear, 1, 1);
    }

    public static LocalDate endExclusiveOf(int bookYear) {
        return startOf(bookYear + 1);
    }
}
