package be.jonasboon.book_keeping_tool.utils;

import be.jonasboon.book_keeping_tool.exceptions.EmtpyArrayException;
import be.jonasboon.book_keeping_tool.utils.mapper.CSVFileMapper;
import be.jonasboon.book_keeping_tool.utils.mapper.CSVObject;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static be.jonasboon.book_keeping_tool.utils.ArrayUtils.isArrayWithValues;

@Slf4j
public class FileReaderUtil {

    public static CSVReader supply(MultipartFile file) {
        try {
            return new CSVReader(new InputStreamReader(file.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<CSVObject> convert(CSVFileMapper mapper, CSVReader reader){
        try {
            List<CSVObject> csvObjects = new ArrayList<>();
            String[] header = reader.readNext();
            log.debug("Headers: {}", Arrays.stream(header).reduce("", (result, item) -> result.concat(", " + item)));
            String[] row;
            while((row = reader.readNext()) != null){
                CSVObject convertedItem = map(row, mapper);
                csvObjects.add(convertedItem);
            }
            reader.close();
            return csvObjects;
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    private static CSVObject map(String[] valuesFromFile, CSVFileMapper mapper){
        try {
            isArrayWithValues(valuesFromFile);
            return mapper.mapToObject(valuesFromFile);
        } catch (EmtpyArrayException e) {
            throw new RuntimeException("The file contains no values");
        }
    }
}