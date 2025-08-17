package be.jonasboon.book_keeping_tool.backup.common;

public interface BackupModelMapper<INPUT, OUTPUT extends BackupModel> {

    OUTPUT of(INPUT input);

}
