package be.javabeans.utils.mapper;

import be.javabeans.utils.CSVUtils;

import java.util.List;

public interface CSVFileMapper {
    CSVObject mapToObject(String[] seperatedValues);
}
