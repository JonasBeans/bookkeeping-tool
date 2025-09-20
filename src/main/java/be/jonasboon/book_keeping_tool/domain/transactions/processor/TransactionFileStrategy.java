package be.jonasboon.book_keeping_tool.domain.transactions.processor;

import be.jonasboon.book_keeping_tool.domain.transactions.entity.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@AllArgsConstructor
public class TransactionFileStrategy {

    private final CSVTransactionProcessor csvTransactionProcessor;
    private final XLSXTransactionProcessor xlsxTransactionProcessor;

    public List<Transaction> process(MultipartFile file, String fileType) {
        return determineProcessor(fileType).process(file);
    }

    private TransactionProcessor determineProcessor(String fileType) {
        switch (fileType) {
            case "csv" -> {
                return csvTransactionProcessor;
            }
            case "xlsx" -> {
                return xlsxTransactionProcessor;
            }
            default -> throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }
    }
}
