package be.jonasboon.book_keeping_tool.backup.common;

public interface RestoreMapper<INPUT, OUTPUT> {

    OUTPUT of(INPUT input);

}
