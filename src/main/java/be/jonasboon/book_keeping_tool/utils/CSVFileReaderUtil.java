package be.jonasboon.book_keeping_tool.utils;

import be.jonasboon.book_keeping_tool.exceptions.EmtpyArrayException;
import be.jonasboon.book_keeping_tool.utils.mapper.CSVFileMapper;
import be.jonasboon.book_keeping_tool.utils.mapper.CSVObject;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static be.jonasboon.book_keeping_tool.utils.ArrayUtils.isArrayWithValues;

@Slf4j
public class CSVFileReaderUtil {

    public static CSVReader consume(MultipartFile file) {
        try {
            return new CSVReader(new InputStreamReader(file.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static CSVReader consume(File file) {
        try {
            return new CSVReader(new InputStreamReader(new FileInputStream(file) ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <O extends CSVObject> List<O> convert(CSVFileMapper mapper, CSVReader reader) {
        return convert(mapper, reader, true);
    }

    public static <O extends CSVObject> List<O> convert(CSVFileMapper mapper, CSVReader reader, boolean headerIndicator) {
        try {
            List<O> csvObjects = new ArrayList<>();
            if (headerIndicator) {
                String[] header = reader.readNext();
                log.debug("Headers: {}", Arrays.stream(header).reduce("", (result, item) -> result.concat(", " + item)));
            }
            String[] row;
            while((row = reader.readNext()) != null){
                O convertedItem = map(row, mapper);
                csvObjects.add(convertedItem);
            }
            reader.close();
            return csvObjects;
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    private static <O extends CSVObject> O map(String[] valuesFromFile, CSVFileMapper mapper){
        try {
            isArrayWithValues(valuesFromFile);
            return mapper.mapToObject(valuesFromFile);
        } catch (EmtpyArrayException e) {
            throw new RuntimeException("The file contains no values");
        }
    }
}