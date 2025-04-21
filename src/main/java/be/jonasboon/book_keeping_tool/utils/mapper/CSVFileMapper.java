package be.jonasboon.book_keeping_tool.utils.mapper;

public interface CSVFileMapper {
    CSVObject mapToObject(String[] seperatedValues);
}
