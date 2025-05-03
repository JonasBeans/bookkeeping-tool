package be.jonasboon.book_keeping_tool.balance_sheet;

import be.jonasboon.book_keeping_tool.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import static be.jonasboon.book_keeping_tool.balance_sheet.BalanceSheetPlotter.*;
import static be.jonasboon.book_keeping_tool.constants.TransactionConstants.ACCOUNTING_WORKSHEET;

@AllArgsConstructor
@Service
@Slf4j
public class BalanceSheetUpdatingService extends BalanceSheetUpdater {

    private final BalanceSheetSaver saver;

    protected void tryProcessToBalanceSheet(Transaction transaction) throws IOException {
        File file = getFullFileName().toFile();
        if (!file.exists()) {
            log.warn("Workbook file does not exist at: {}. Creating new workbook.", getFullFileName());
            try (XSSFWorkbook newWorkbook = new XSSFWorkbook()) {
                newWorkbook.createSheet("Transactions");
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    newWorkbook.write(fos);
                }
            }
        }

        try (Workbook workbook = readSheet()) {
            Sheet rows = workbook.getSheetAt(ACCOUNTING_WORKSHEET);
            Integer firstEmptyRowIndex = getFirstEmptyRowIndex(rows);
            rows.createRow(firstEmptyRowIndex);
            appendDate(rows, firstEmptyRowIndex, transaction.getTransactionDate());
            appendAmount(rows, firstEmptyRowIndex, transaction.getAmount());
            appendNameOfOtherParty(rows, firstEmptyRowIndex, transaction.getNameOtherParty());
            appendCostCenter(rows, firstEmptyRowIndex, transaction.getCostCenterIndex(), transaction.getAmount());
            saveChanges(workbook);
        }
    }

    @Override
    protected Path getFullFileName() {
        return saver.getFullFilePath();
    }

    protected Workbook readSheet() throws IOException {
        File file = getFullFileName().toFile();
        if (!file.exists()) {
            throw new IOException("Workbook file does not exist at: " + getFullFileName());
        }
        try (FileInputStream accountingWorkbook = new FileInputStream(file)) {
            return new XSSFWorkbook(accountingWorkbook);
        }
    }

    @Override
    protected void saveChanges(Workbook workbookToSave) {
        File file = getFullFileName().toFile();
        if (!file.exists()) {
            log.warn("Creating new workbook file at: {}", getFullFileName());
        }
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbookToSave.write(fos);
            log.debug("Successfully saved workbook to: {}", getFullFileName());
        } catch (IOException e) {
            log.error("Failed to save workbook to: {}", getFullFileName(), e);
            throw new RuntimeException("Failed to save workbook: " + e.getMessage(), e);
        }
    }

}
