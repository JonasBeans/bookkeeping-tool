package be.jonasboon.book_keeping_tool.domain.transactions.processor;

import be.jonasboon.book_keeping_tool.domain.transactions.entity.Transaction;
import be.jonasboon.book_keeping_tool.domain.transactions.mapper.TransactionCSVMapper;
import be.jonasboon.book_keeping_tool.domain.transactions.mapper.TransactionMapper;
import be.jonasboon.book_keeping_tool.utils.CSVFileReaderUtil;
import com.opencsv.CSVReader;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
public class CSVTransactionProcessor implements TransactionProcessor {

    private final TransactionMapper transactionMapper;

    public CSVTransactionProcessor(TransactionMapper transactionMapper) {
        this.transactionMapper = transactionMapper;
    }

    @Override
    public List<Transaction> process(MultipartFile file) {
        try (CSVReader reader = CSVFileReaderUtil.consume(file)) {
            return CSVFileReaderUtil
                    .convert(new TransactionCSVMapper(), reader)
                    .stream()
                    .map(transactionMapper::map)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
