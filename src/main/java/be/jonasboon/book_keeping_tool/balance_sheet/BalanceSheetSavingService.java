package be.jonasboon.book_keeping_tool.balance_sheet;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Service
@Slf4j
public class BalanceSheetSavingService extends BalanceSheetSaver {

    @Getter(AccessLevel.PROTECTED)
    @Value(value = "${workbook.file.location}")
    private String balanceSheetWorkingDirectory;

    @Setter(AccessLevel.PROTECTED)
    @Getter(AccessLevel.PUBLIC)
    private String balanceSheetFileName;

    @Override
    public void saveBalanceSheet(MultipartFile file) throws IOException {
        log.info("Saving balance sheet file: {}", file.getOriginalFilename());
        File directory = new File(getBalanceSheetWorkingDirectory());
        if (!directory.exists()) {
            directory.mkdirs();
        }
        setBalanceSheetFileName(file.getOriginalFilename());
        Files.copy(file.getInputStream(), getFullFilePath(), StandardCopyOption.REPLACE_EXISTING);
        log.info("Successfully save balance sheet file: {}", file.getOriginalFilename());
    }

}
