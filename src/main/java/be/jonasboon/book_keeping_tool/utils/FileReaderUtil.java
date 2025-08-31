package be.jonasboon.book_keeping_tool.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReaderUtil {

    public static <I> List<I> convertFileToType(String fileName, ObjectMapper objectMapper, ParameterizedTypeReference<I> reference) {
        List<I> items = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = reader.readLine()) != null) {
                I convertedItem = objectMapper.readValue(line, objectMapper.constructType(reference.getType()));
                items.add(convertedItem);
                System.out.println(convertedItem);
            }

            return items;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + fileName, e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
