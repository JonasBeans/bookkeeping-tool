package be.javabeans.utils;

import java.util.function.Function;
import java.util.function.Predicate;

public class StringUtils {
    public static Predicate<String> isNull = value -> value.trim() == null;
    public static Predicate<String> isBlank = String::isBlank;
    public static Predicate<String> isEmpty = String::isEmpty;
    public static Predicate<String> validateString = isNull.and(isBlank).and(isEmpty).negate();

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
