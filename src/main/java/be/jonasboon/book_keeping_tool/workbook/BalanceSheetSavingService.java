package be.jonasboon.book_keeping_tool.workbook;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Service
public class BalanceSheetSavingService extends BalanceSheetSaver {

    @Getter(AccessLevel.PROTECTED)
    @Value(value = "${workbook.file.location}")
    private String balanceSheetWorkingDirectory;

    @Setter(AccessLevel.PROTECTED)
    @Getter(AccessLevel.PROTECTED)
    private String balanceSheetFileName;

    @Override
    public void saveBalanceSheet(MultipartFile file) throws IOException {
        File directory = new File(getBalanceSheetWorkingDirectory());
        if (!directory.exists()) {
            directory.mkdirs();
        }
        setBalanceSheetFileName(file.getOriginalFilename());
        Files.copy(file.getInputStream(), getFullFilePath(), StandardCopyOption.REPLACE_EXISTING);
    }

}
