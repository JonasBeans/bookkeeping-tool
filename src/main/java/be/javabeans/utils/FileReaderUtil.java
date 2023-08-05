package be.javabeans.utils;

import be.javabeans.exceptions.EmtpyArrayException;
import be.javabeans.utils.mapper.CSVFileMapper;
import be.javabeans.utils.mapper.CSVObject;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static be.javabeans.utils.ArrayUtils.isArrayWithValues;

@Slf4j
public class FileReaderUtil {

    private File file;

    public FileReaderUtil(String fileName){
        this.file = new File(fileName);
    };

    public List<CSVObject> readFile(CSVFileMapper mapper){
        try {
            List<CSVObject> csvObjects = new ArrayList<>();
            CSVReader reader = new CSVReader(new FileReader(file));
            String[] header = reader.readNext();
            Arrays.stream(header).forEach(log::info);
            String[] row;
            while((row = reader.readNext()) != null){
                CSVObject convertedItem = readAndConvert(row, mapper);
                csvObjects.add(convertedItem);
            }
            reader.close();
            return csvObjects;
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    private CSVObject readAndConvert(String[] valuesFromFile, CSVFileMapper mapper){
        try {
            isArrayWithValues(valuesFromFile);
            return mapper.mapToObject(valuesFromFile);
        } catch (EmtpyArrayException e) {
            throw new RuntimeException("The file contains no values");
        }
    }
}