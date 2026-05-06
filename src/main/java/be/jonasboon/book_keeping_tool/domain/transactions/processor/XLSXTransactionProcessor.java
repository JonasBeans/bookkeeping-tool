package be.jonasboon.book_keeping_tool.domain.transactions.processor;

import be.jonasboon.book_keeping_tool.domain.transactions.entity.Transaction;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class XLSXTransactionProcessor implements TransactionProcessor {

    @Override
    public List<Transaction> process(MultipartFile file) {
        List<Transaction> transactions = new ArrayList<>();
        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();
            boolean isHeader = true;
            for (Row row : sheet) {
                if (isHeader) { isHeader = false; continue; }
                Transaction.TransactionBuilder builder = Transaction.builder();
                Cell bookDateCell = row.getCell(1);
                Cell descriptionCell = row.getCell(4);
                Cell transactionDateCell = row.getCell(7);
                Cell amountCell = row.getCell(5);
                Cell nameOtherPartyCell = row.getCell(9);
                Cell messageCell = row.getCell(10);
                // Parse bookDate
                if (bookDateCell != null && bookDateCell.getCellType() == CellType.STRING) {
                    builder.withBookDate(LocalDate.parse(bookDateCell.getStringCellValue(), DateTimeFormatter.ISO_DATE));
                } else if (bookDateCell != null && bookDateCell.getCellType() == CellType.NUMERIC) {
                    builder.withBookDate(bookDateCell.getLocalDateTimeCellValue().toLocalDate());
                }
                // Parse transactionDate
                if (transactionDateCell != null && transactionDateCell.getCellType() == CellType.STRING) {
                    builder.withTransactionDate(LocalDate.parse(transactionDateCell.getStringCellValue(), DateTimeFormatter.ISO_DATE));
                } else if (transactionDateCell != null && transactionDateCell.getCellType() == CellType.NUMERIC) {
                    builder.withTransactionDate(transactionDateCell.getLocalDateTimeCellValue().toLocalDate());
                }
                // Parse description
                if (descriptionCell != null) {
                    builder.withDescription(dataFormatter.formatCellValue(descriptionCell));
                }
                // Parse amount
                if (amountCell != null && amountCell.getCellType() == CellType.NUMERIC) {
                    builder.withAmount(BigDecimal.valueOf(amountCell.getNumericCellValue()));
                } else if (amountCell != null && amountCell.getCellType() == CellType.STRING) {
                    builder.withAmount(new BigDecimal(amountCell.getStringCellValue()));
                }
                // Parse nameOtherParty
                if (nameOtherPartyCell != null) {
                    builder.withNameOtherParty(dataFormatter.formatCellValue(nameOtherPartyCell));
                }
                // Parse message
                if (messageCell != null) {
                    builder.withMessage(dataFormatter.formatCellValue(messageCell));
                }
                transactions.add(builder.build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to process XLSX file", e);
        }
        return transactions;
    }
}
