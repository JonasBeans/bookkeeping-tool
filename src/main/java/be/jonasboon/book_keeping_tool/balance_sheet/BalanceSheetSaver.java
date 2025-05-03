package be.jonasboon.book_keeping_tool.balance_sheet;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public abstract class BalanceSheetSaver {

    public abstract void saveBalanceSheet(MultipartFile file) throws IOException;

    public abstract String getBalanceSheetFileName();
    protected abstract String getBalanceSheetWorkingDirectory();
    protected abstract void setBalanceSheetFileName(String fileName);

    public Path getFullFilePath() {
        if (getBalanceSheetFileName() == null) { throw new IllegalStateException("Balance sheet not uploaded yet."); }
        return Path.of(getBalanceSheetWorkingDirectory(), getBalanceSheetFileName());
    }

}
