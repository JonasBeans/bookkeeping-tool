package be.jonasboon.book_keeping_tool.utils;

import java.util.function.Function;
import java.util.function.Predicate;

public class StringUtils {
    public static Predicate<String> isNull = value -> value.trim() == null;
    public static Predicate<String> isBlank = String::isBlank;
    public static Predicate<String> isEmpty = String::isEmpty;
    private static Predicate<String> isYes = (value) -> value.equalsIgnoreCase("y");
    private static Predicate<String> isNo = (value) -> value.equalsIgnoreCase("n");
    public static Predicate<String> validateString = isNull.and(isBlank).and(isEmpty).negate();
    public static Predicate<String> validateConfirmation = validateString.and(isYes).or(isNo);

    /**
     * @Case: To convert a string to BigDecimal the European way of representing a decimal is not possible.
     * That's why converting "," to "." is needed.
     */
    public static Function<String, String> convertCommaToPoint = (value) -> value.replace(",",".");

    /**
     * @Case: To convert a string to BigDecimal the European way of representing a decimal is not possible.
     * That's why separating points should be removed.
     */
    public static Function<String, String> removeSeperatingPoint = (value) -> value.replace(".","");

    /**
     * @Description Convert a European Decimal for example to convert a string to a BigDecimal
     * @Example 2.000,00 => 2000,00
     */
    public static Function<String,String> convertEuropeanDecimal = removeSeperatingPoint.andThen(convertCommaToPoint);
}
