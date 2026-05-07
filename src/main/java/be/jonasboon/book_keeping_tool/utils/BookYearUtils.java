package be.jonasboon.book_keeping_tool.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.YearMonth;

@UtilityClass
public class BookYearUtils {

    public static LocalDate startOf(int bookYear) {
        return LocalDate.of(bookYear, 1, 1);
    }

    public static LocalDate endExclusiveOf(int bookYear) {
        return startOf(bookYear + 1);
    }

    public static LocalDate startOf(int bookYear, Integer bookMonth) {
        if (bookMonth == null) {
            return startOf(bookYear);
        }
        return YearMonth.of(bookYear, bookMonth).atDay(1);
    }

    public static LocalDate endExclusiveOf(int bookYear, Integer bookMonth) {
        if (bookMonth == null) {
            return endExclusiveOf(bookYear);
        }
        return YearMonth.of(bookYear, bookMonth).plusMonths(1).atDay(1);
    }
}
