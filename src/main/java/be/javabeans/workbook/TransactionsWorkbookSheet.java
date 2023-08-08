package be.javabeans.workbook;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;

import static be.javabeans.constants.TransactionConstants.ACCOUNTING_WORKSHEET;
import static be.javabeans.constants.TransactionConstants.CellValues.DATE_CELL;
import static java.lang.String.format;

/**
 * The accounting workbook is in the .xlsx format.
 * This class is for using the transactions worksheet.
 */
public class TransactionsWorkbookSheet {
    private final String workbooklocation;
    public TransactionsWorkbookSheet(String workbooklocation) {
        this.workbooklocation = workbooklocation;
    }

    public void appendDate(LocalDate date) throws IOException {
        Workbook workbook = readSheet();
        Sheet rows = workbook.getSheetAt(ACCOUNTING_WORKSHEET);
        rows.createRow(getFirstEmptyRowIndex(rows))
                .createCell(DATE_CELL)
                .setCellValue(date);
        saveChanges(workbook);
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
