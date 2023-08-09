package be.javabeans.workbook;

import be.javabeans.model.Transaction;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import static be.javabeans.constants.TransactionConstants.ACCOUNTING_WORKSHEET;
import static be.javabeans.constants.TransactionConstants.CellValues.*;

/**
 * The accounting workbook is in the .xlsx format.
 * This class is for using the transactions worksheet.
 */
public class TransactionsWorkbookSheet {
    private final String workbooklocation;
    public TransactionsWorkbookSheet(String workbooklocation) {
        this.workbooklocation = workbooklocation;
    }

    public void processTransactionToAccountingFile(Transaction transaction) throws IOException {
        Workbook workbook = readSheet();
        Sheet rows = workbook.getSheetAt(ACCOUNTING_WORKSHEET);
        Integer firstEmptyRowIndex = getFirstEmptyRowIndex(rows);
        rows.createRow(firstEmptyRowIndex);
        appendDate(rows, firstEmptyRowIndex,transaction.getTransactionDate());
        appendAmount(rows, firstEmptyRowIndex, transaction.getAmount());
        appendNameOfOtherParty(rows, firstEmptyRowIndex, transaction.getNameOtherParty());
        appendCostCenter(rows, firstEmptyRowIndex, transaction.getCostCenterIndex(), transaction.getAmount());
        saveChanges(workbook);
    }

    private void appendNameOfOtherParty(Sheet row, Integer index, String nameOtherParty) {
        row.getRow(index).createCell(NAME_OTHER_PARTY_CELL).setCellValue(nameOtherParty);
    }

    private void appendAmount(Sheet row, Integer index ,BigDecimal amount) {
        row.getRow(index).createCell(AMOUNT_CELL).setCellValue(amount.doubleValue());
    }

    private void appendCostCenter(Sheet row, Integer index, Integer costCenterIndex, BigDecimal amount) {
        row.getRow(index).createCell(costCenterIndex).setCellValue(Math.abs(amount.doubleValue()));
    }

    private void appendDate(Sheet row, Integer index , LocalDate date) {
        row.getRow(index).createCell(DATE_CELL).setCellValue(date);
    }

    private Workbook readSheet() throws IOException {
        FileInputStream accountingWorkbook = new FileInputStream(workbooklocation);
        return new XSSFWorkbook(accountingWorkbook);
    }

    private Integer getFirstEmptyRowIndex(Sheet rows) {
        Integer foundIndex = 0;
        for (Row row : rows) {
            Cell cell = row.getCell(DATE_CELL);
            if(cell == null || cell.getCellType() == CellType.BLANK){
                break;
            }
            foundIndex++;
        }
        return foundIndex;
    }

    private void saveChanges(Workbook workbookToSave){
        try (FileOutputStream fos = new FileOutputStream(workbooklocation)){
            workbookToSave.write(fos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
