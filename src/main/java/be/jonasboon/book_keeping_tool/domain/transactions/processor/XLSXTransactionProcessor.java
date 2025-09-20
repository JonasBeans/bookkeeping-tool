package be.jonasboon.book_keeping_tool.domain.transactions.processor;

import be.jonasboon.book_keeping_tool.domain.transactions.entity.Transaction;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class XLSXTransactionProcessor implements TransactionProcessor {

    @Override
    public List<Transaction> process(MultipartFile file) {
        return List.of();
    }
}
