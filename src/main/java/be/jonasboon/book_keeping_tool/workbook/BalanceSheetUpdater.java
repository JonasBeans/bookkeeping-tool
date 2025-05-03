package be.jonasboon.book_keeping_tool.workbook;

import be.jonasboon.book_keeping_tool.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;

import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public abstract class BalanceSheetUpdater {

    public void processTransactionToAccountingFile(Transaction transaction) {
        try {
            checkFileExists();
            log.debug("Processing transaction... {}", transaction);
            tryProcessToBalanceSheet(transaction);
            log.debug("Processed transaction! {}", transaction);
        } catch (Exception e) {
            log.error("Failed to process transaction: {}", transaction, e);
            throw new RuntimeException("Failed to process transaction: " + e.getMessage(), e);
        }
    }

    private void checkFileExists() {
        if (!Files.exists(getFullFileName())) throw new IllegalStateException("Workbook file not found: " + getFullFileName());
    }

    protected abstract void saveChanges(Workbook workbookToSave);
    protected abstract void tryProcessToBalanceSheet(Transaction transaction) throws Exception;
    protected abstract Path getFullFileName();

}
