package be.jonasboon.book_keeping_tool.utils;

import be.jonasboon.book_keeping_tool.exceptions.EmtpyArrayException;

import java.util.Optional;
import java.util.function.Predicate;

import static be.jonasboon.book_keeping_tool.utils.StringUtils.*;

public class ArrayUtils {
    static Predicate<Object[]> isArrayEmpty = array -> array.length == 0;

    public static String getValueFromArray(int index, String[] array){
        if(isArrayEmpty.test(array)) throw new RuntimeException("No arguments given");
        Optional<String> result = Optional.ofNullable(array[index]);
        return result
                .filter(isEmpty.and(isNull).negate())
                .orElseThrow(() -> new RuntimeException("No argument given"));
    }

    public static boolean isArrayWithValues(Object[] arrayToValidate) throws EmtpyArrayException {
        if (isArrayEmpty.test(arrayToValidate))
            throw new EmtpyArrayException("Array is empty");
        return true;
    }
}
