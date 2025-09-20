package be.jonasboon.book_keeping_tool.domain.transactions.processor;

import be.jonasboon.book_keeping_tool.domain.transactions.entity.Transaction;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TransactionProcessor {
    List<Transaction> process(MultipartFile file);
}
