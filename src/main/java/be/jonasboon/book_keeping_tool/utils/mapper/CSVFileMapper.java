package be.jonasboon.book_keeping_tool.utils.mapper;

public interface CSVFileMapper {
    <O extends CSVObject> O mapToObject(String[] seperatedValues);
}
