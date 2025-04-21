package be.jonasboon.book_keeping_tool.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CSVUtils {

    public static List<String> seperateValues(String incomingValue, String delimiter){
        return Arrays.stream(incomingValue.split(delimiter)).collect(Collectors.toList());
    }
}
