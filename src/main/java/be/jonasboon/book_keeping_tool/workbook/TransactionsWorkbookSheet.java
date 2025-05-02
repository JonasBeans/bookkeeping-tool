package be.jonasboon.book_keeping_tool.workbook;

import be.jonasboon.book_keeping_tool.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import static be.jonasboon.book_keeping_tool.constants.TransactionConstants.ACCOUNTING_WORKSHEET;
import static be.jonasboon.book_keeping_tool.constants.TransactionConstants.CellValues.*;

/**
 * The accounting workbook is in the .xlsx format.
 * This class is for using the transactions' worksheet.
 */
@Service
@Slf4j
public class TransactionsWorkbookSheet {

    @Value(value = "${workbook.file.location}")
    String workbookFileLocation;

    public void processTransactionToAccountingFile(Transaction transaction) {
        try {
            log.debug("Processing transaction... {}", transaction);
            tryProcessTransactionToAccountingFile(transaction);
            log.debug("Processed transaction! {}", transaction);
        } catch (Exception e) {
            log.error("Failed to process transaction: {}", transaction, e);
            throw new RuntimeException("Failed to process transaction: " + e.getMessage(), e);
        }
    }

    private void tryProcessTransactionToAccountingFile(Transaction transaction) throws IOException {
        File file = new File(workbookFileLocation);
        if (!file.exists()) {
            log.warn("Workbook file does not exist at: {}. Creating new workbook.", workbookFileLocation);
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

    private void appendNameOfOtherParty(Sheet row, Integer index, String nameOtherParty) {
        row.getRow(index).createCell(NAME_OTHER_PARTY_CELL).setCellValue(nameOtherParty);
    }

    private void appendAmount(Sheet row, Integer index, BigDecimal amount) {
        row.getRow(index).createCell(AMOUNT_CELL).setCellValue(amount.doubleValue());
    }

    private void appendCostCenter(Sheet row, Integer index, Integer costCenterIndex, BigDecimal amount) {
        row.getRow(index).createCell(costCenterIndex).setCellValue(Math.abs(amount.doubleValue()));
    }

    private void appendDate(Sheet row, Integer index, LocalDate date) {
        row.getRow(index).createCell(DATE_CELL).setCellValue(date);
    }

    private Workbook readSheet() throws IOException {
        File file = new File(workbookFileLocation);
        if (!file.exists()) {
            throw new IOException("Workbook file does not exist at: " + workbookFileLocation);
        }
        try (FileInputStream accountingWorkbook = new FileInputStream(file)) {
            return new XSSFWorkbook(accountingWorkbook);
        }
    }

    private Integer getFirstEmptyRowIndex(Sheet rows) {
        Integer foundIndex = 0;
        for (Row row : rows) {
            Cell cell = row.getCell(DATE_CELL);
            if (cell == null || cell.getCellType() == CellType.BLANK) {
                break;
            }
            foundIndex++;
        }
        return foundIndex;
    }

    private void saveChanges(Workbook workbookToSave) {
        File file = new File(workbookFileLocation);
        if (!file.exists()) {
            log.warn("Creating new workbook file at: {}", workbookFileLocation);
        }
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbookToSave.write(fos);
            log.debug("Successfully saved workbook to: {}", workbookFileLocation);
        } catch (IOException e) {
            log.error("Failed to save workbook to: {}", workbookFileLocation, e);
            throw new RuntimeException("Failed to save workbook: " + e.getMessage(), e);
        }
    }
}
