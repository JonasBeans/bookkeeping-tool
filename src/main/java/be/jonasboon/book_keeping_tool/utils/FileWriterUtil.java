package be.jonasboon.book_keeping_tool.utils;

import com.opencsv.CSVWriter;

import java.io.FileWriter;

public class FileWriterUtil {

    public static void writeToCSVFile(String filePath, String[] content) {
        CSVWriter writer = null;
        try {
             writer = new CSVWriter(new FileWriter(filePath));
            for (String line : content) {
                String[] values = line.split(",");
                writer.writeNext(values);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error writing to file: " + filePath, e);
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
                throw new RuntimeException("Error closing the writer", e);
            }
        }
    }

    public static void writeToJsonFile(String filePath, String[] content) {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (String line : content) {
                writer.write(line);
                writer.write(System.lineSeparator());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error writing to file: " + filePath, e);
        }
    }
}
