package be.jonasboon.book_keeping_tool.workbook;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.math.BigDecimal;
import java.time.LocalDate;

import static be.jonasboon.book_keeping_tool.constants.TransactionConstants.CellValues.*;

public class BalanceSheetPlotter {

    protected static void appendNameOfOtherParty(Sheet row, Integer index, String nameOtherParty) {
        row.getRow(index).createCell(NAME_OTHER_PARTY_CELL).setCellValue(nameOtherParty);
    }

    protected static void appendAmount(Sheet row, Integer index, BigDecimal amount) {
        row.getRow(index).createCell(AMOUNT_CELL).setCellValue(amount.doubleValue());
    }

    protected static void appendCostCenter(Sheet row, Integer index, Integer costCenterIndex, BigDecimal amount) {
        row.getRow(index).createCell(costCenterIndex).setCellValue(Math.abs(amount.doubleValue()));
    }

    protected static void appendDate(Sheet row, Integer index, LocalDate date) {
        row.getRow(index).createCell(DATE_CELL).setCellValue(date);
    }

    protected static Integer getFirstEmptyRowIndex(Sheet rows) {
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

}
